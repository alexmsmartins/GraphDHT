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

import org.neo4j.benchmark.OperationCounter.Operation;

public class OperationCounterImplTest extends TestCase
{
    public OperationCounterImplTest( String name )
    {
        super( name );
    }

    public void testOneOfEachOperation()
    {
        OperationCounter operationCounter = new OperationCounterImpl();
        // one of each op
        operationCounter.neoGetReferenceNode();
        operationCounter.neoGetNodeById();
        operationCounter.neoGetRelationshipById();
        operationCounter.relationshipCreate();
        operationCounter.relationshipDelete();
        operationCounter.relationshipGetStartNode();
        operationCounter.relationshipGetEndNode();
        operationCounter.relationshipGetOtherNode();
        operationCounter.relationshipGetNodes();
        operationCounter.nodeCreate();
        operationCounter.nodeDelete();
        operationCounter.nodeGetRelationships();
        operationCounter.nodeHasRelationship();
        operationCounter.nodeGetSingleRelationship();
        operationCounter.PropertyContainerHasProperty();
        operationCounter.PropertyContainerGetProperty();
        operationCounter.PropertyContainerSetProperty();
        operationCounter.PropertyContainerRemoveProperty();
        operationCounter.PropertyContainerGetPropertyKeys();
        operationCounter.PropertyContainerGetPropertyValues();
        // assert we have one of each
        for ( Operation operation : Operation.values() )
        {
            assertTrue( operationCounter.getNumberOfOperations( operation ) == 1 );
        }
        assertTrue( operationCounter.getTotalWeightForAllOperations() == Operation
            .values().length );
        for ( Operation operation : Operation.values() )
        {
            assertTrue( operationCounter.getOperationWeight( operation ) == OperationCounter.standardOperationWeight );
        }
        // set different weights on all ops
        int w = 1;
        long correctSum = 0;
        for ( Operation operation : Operation.values() )
        {
            operationCounter.setOperationWeight( operation, w );
            assertTrue( operationCounter.getOperationWeight( operation ) == w );
            correctSum += w;
            ++w;
        }
        assertTrue( operationCounter.getTotalWeightForAllOperations() == correctSum );
    }
}
