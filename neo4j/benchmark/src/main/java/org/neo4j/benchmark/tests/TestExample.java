/*
 * Copyright (c) 2008 "Neo Technology,"
 *     Network Engine for Objects in Lund AB [http://neotechnology.com]
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.benchmark.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;
import org.neo4j.benchmark.OperationCounter;
import org.neo4j.benchmark.Test;

/**
 * Small example of how a test could work.
 * @author Patrik
 */
public class TestExample implements Test
{
    protected static enum MyRelTypes implements RelationshipType
    {
        NEXT
    }

    Map<String,String> parameters;
    NeoService neo;
    // This declares what subtests exists and in what order they should be run.
    String[] subTestIdentifiers = { "Create", "Read", "MultiThreadRead" };

    /**
     * Dispatch method
     */
    public void runSubTest( String subTestIdentifier,
        OperationCounter operationCounter )
    {
        if ( "Create".equals( subTestIdentifier ) )
        {
            runCreate( operationCounter );
        }
        else if ( "Read".equals( subTestIdentifier ) )
        {
            runRead( operationCounter );
        }
        else if ( "MultiThreadRead".equals( subTestIdentifier ) )
        {
            runMultiThreadRead( operationCounter );
        }
    }

    public String[] getSubTestIdentifiers()
    {
        return subTestIdentifiers;
    }

    public String getIdentifier()
    {
        return "TestExample";
    }

    public String getDescription()
    {
        return "Create and read some objects";
    }

    public void Init( Map<String,String> parameters, NeoService neo )
    {
        this.parameters = parameters;
        this.neo = neo;
    }

    public void TearDown()
    {
    }

    // This creates a linked list
    public void runCreate( OperationCounter oc )
    {
        Transaction tx = neo.beginTx();
        oc.txBegin( tx );
        // AutoRenewedTransaction tx = new AutoRenewedTransaction( neo, 10000 );
        long objects = Long.parseLong( parameters.get( "objects" ) );
        Node prevNode = neo.getReferenceNode();
        for ( long l = 0; l < objects; ++l )
        {
            // Create object
            Node node = neo.createNode();
            // Create relationship
            prevNode.createRelationshipTo( node, MyRelTypes.NEXT );
            // Report
            oc.nodeCreate().relationshipCreate();
            // Set prev
            prevNode = node;
        }
        tx.success();
        oc.txBeforeFinish( tx );
        tx.finish();
        oc.txEnd( tx );
    }

    public void runRead( OperationCounter oc )
    {
        Transaction tx = neo.beginTx();
        oc.txBegin( tx );
        Node node = neo.getReferenceNode();
        Relationship relationship = node.getSingleRelationship(
            MyRelTypes.NEXT, Direction.OUTGOING );
        while ( relationship != null )
        {
            node = relationship.getEndNode();
            // Report
            oc.nodeGetSingleRelationship().relationshipGetEndNode();
            relationship = node.getSingleRelationship( MyRelTypes.NEXT,
                Direction.OUTGOING );
        }
        tx.success();
        oc.txBeforeFinish( tx );
        tx.finish();
        oc.txEnd( tx );
    }

    public void runMultiThreadRead( OperationCounter oc )
    {
        // Create 10 threads
        List<Thread> threads = new LinkedList<Thread>();
        for ( int i = 0; i < 10; ++i )
        {
            threads.add( new ReadThread( oc ) );
        }
        // Run!
        for ( Thread thread : threads )
        {
            thread.start();
        }
        // Join
        for ( Thread thread : threads )
        {
            try
            {
                thread.join();
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread object performing a read identical to the "Read" subtest
     */
    class ReadThread extends Thread
    {
        private OperationCounter oc;

        public ReadThread( OperationCounter oc )
        {
            super();
            this.oc = oc;
        }

        public void run()
        {
            Transaction tx = neo.beginTx();
            oc.txBegin( tx );
            Node node = neo.getReferenceNode();
            Relationship relationship = node.getSingleRelationship(
                MyRelTypes.NEXT, Direction.OUTGOING );
            while ( relationship != null )
            {
                node = relationship.getEndNode();
                // Report
                oc.nodeGetSingleRelationship().relationshipGetEndNode();
                relationship = node.getSingleRelationship( MyRelTypes.NEXT,
                    Direction.OUTGOING );
            }
            tx.success();
            oc.txBeforeFinish( tx );
            tx.finish();
            oc.txEnd( tx );
        }
    }
}
