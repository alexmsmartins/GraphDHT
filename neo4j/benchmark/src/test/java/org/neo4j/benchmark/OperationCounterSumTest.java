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
package org.neo4j.benchmark;

import junit.framework.TestCase;

import org.neo4j.api.core.Transaction;
import org.neo4j.benchmark.OperationCounter.Operation;

public class OperationCounterSumTest extends TestCase
{
    public OperationCounterSumTest( String name )
    {
        super( name );
    }

    public void testSum()
    {
        OperationCounterSum operationCounterSum = new OperationCounterSum();
        StaticOpCounter oc5 = new StaticOpCounter( 5 );
        StaticOpCounter oc7 = new StaticOpCounter( 7 );
        assertValue( operationCounterSum, 0 );
        operationCounterSum.addCounter( oc5 );
        assertValue( operationCounterSum, 5 );
        operationCounterSum.addCounter( oc7 );
        assertValue( operationCounterSum, 12 );
        operationCounterSum.removeCounter( oc5 );
        assertValue( operationCounterSum, 7 );
        operationCounterSum.removeCounter( oc7 );
        assertValue( operationCounterSum, 0 );
    }

    private void assertValue( OperationCounterSum operationCounterSum, int value )
    {
        for ( Operation operation : Operation.values() )
        {
            assertTrue( operationCounterSum.getNumberOfOperations( operation ) == value );
        }
        assertTrue( operationCounterSum.getTotalWeightForAllOperations() == value );
    }

    /**
     * An {@link OperationCounter} that returns a certain value for all
     * applicable get methods.
     * @author Patrik
     */
    private class StaticOpCounter implements OperationCounter
    {
        int value = 0;

        public StaticOpCounter( int value )
        {
            super();
            this.value = value;
        }

        public OperationCounter PropertyContainerGetProperty()
        {
            return null;
        }

        public OperationCounter PropertyContainerGetPropertyKeys()
        {
            return null;
        }

        public OperationCounter PropertyContainerGetPropertyValues()
        {
            return null;
        }

        public OperationCounter PropertyContainerHasProperty()
        {
            return null;
        }

        public OperationCounter PropertyContainerRemoveProperty()
        {
            return null;
        }

        public OperationCounter PropertyContainerSetProperty()
        {
            return null;
        }

        public long getInCommitTimeNanos()
        {
            return value;
        }

        public long getNumberOfOperations( Operation operation )
        {
            return value;
        }

        public float getOperationWeight( Operation operation )
        {
            return value;
        }

        public long getPreCommitTimeNanos()
        {
            return value;
        }

        public double getScore()
        {
            return value;
        }

        public long getTotalTimeNanos()
        {
            return value;
        }

        public long getTotalWeightForAllOperations()
        {
            return value;
        }

        public OperationCounter neoGetNodeById()
        {
            return null;
        }

        public OperationCounter neoGetReferenceNode()
        {
            return null;
        }

        public OperationCounter neoGetRelationshipById()
        {
            return null;
        }

        public OperationCounter nodeCreate()
        {
            return null;
        }

        public OperationCounter nodeDelete()
        {
            return null;
        }

        public OperationCounter nodeGetRelationships()
        {
            return null;
        }

        public OperationCounter nodeGetSingleRelationship()
        {
            return null;
        }

        public OperationCounter nodeHasRelationship()
        {
            return null;
        }

        public OperationCounter relationshipCreate()
        {
            return null;
        }

        public OperationCounter relationshipDelete()
        {
            return null;
        }

        public OperationCounter relationshipGetEndNode()
        {
            return null;
        }

        public OperationCounter relationshipGetNodes()
        {
            return null;
        }

        public OperationCounter relationshipGetOtherNode()
        {
            return null;
        }

        public OperationCounter relationshipGetStartNode()
        {
            return null;
        }

        public void setOperationWeight( Operation operation, float weight )
        {
        }

        public OperationCounter timeBegin()
        {
            return null;
        }

        public OperationCounter timeEnd()
        {
            return null;
        }

        public OperationCounter txBeforeFinish( Transaction transaction )
        {
            return null;
        }

        public OperationCounter txBegin( Transaction transaction )
        {
            return null;
        }

        public OperationCounter txEnd( Transaction transaction )
        {
            return null;
        }
    }
}
