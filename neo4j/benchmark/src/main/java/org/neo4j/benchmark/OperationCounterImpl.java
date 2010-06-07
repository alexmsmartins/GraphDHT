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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.neo4j.api.core.Transaction;

/**
 * Primary implementation of {@link OperationCounter}.
 * @author Patrik
 */
public class OperationCounterImpl implements OperationCounter
{
    AtomicInteger neoGetReferenceNodeCounter = new AtomicInteger();
    AtomicInteger neoGetNodeByIdCounter = new AtomicInteger();
    AtomicInteger neoGetRelationshipByIdCounter = new AtomicInteger();
    AtomicInteger relationshipCreateCounter = new AtomicInteger();
    AtomicInteger relationshipDeleteCounter = new AtomicInteger();
    AtomicInteger relationshipGetStartNodeCounter = new AtomicInteger();
    AtomicInteger relationshipGetEndNodeCounter = new AtomicInteger();
    AtomicInteger relationshipGetOtherNodeCounter = new AtomicInteger();
    AtomicInteger relationshipGetNodesCounter = new AtomicInteger();
    AtomicInteger nodeCreateCounter = new AtomicInteger();
    AtomicInteger nodeDeleteCounter = new AtomicInteger();
    AtomicInteger nodeGetRelationshipsCounter = new AtomicInteger();
    AtomicInteger nodeHasRelationshipCounter = new AtomicInteger();
    AtomicInteger nodeGetSingleRelationshipCounter = new AtomicInteger();
    AtomicInteger PropertyContainerHasPropertyCounter = new AtomicInteger();
    AtomicInteger PropertyContainerGetPropertyCounter = new AtomicInteger();
    AtomicInteger PropertyContainerSetPropertyCounter = new AtomicInteger();
    AtomicInteger PropertyContainerRemovePropertyCounter = new AtomicInteger();
    AtomicInteger PropertyContainerGetPropertyKeysCounter = new AtomicInteger();
    AtomicInteger PropertyContainerGetPropertyValuesCounter = new AtomicInteger();
    // Map for holding different weights for different operations. Used when a
    // total metric is calculated.
    Map<Operation,Float> operationWeights = new HashMap<Operation,Float>();
    // Longs for keeping track of time
    AtomicLong preCommitTime = new AtomicLong();
    AtomicLong inCommitTime = new AtomicLong();
    long startTime, endTime;
    // Map for keeping the times for each transaction
    Map<Transaction,transactionTimeHolder> transactionTimes = new ConcurrentHashMap<Transaction,transactionTimeHolder>();

    // Structure to keep in the map above
    protected class transactionTimeHolder
    {
        public long start, middle, end;
    }

    public OperationCounter timeBegin()
    {
        startTime = System.nanoTime();
        return this;
    }

    public OperationCounter timeEnd()
    {
        endTime = System.nanoTime();
        return this;
    }

    public long getTotalTimeNanos()
    {
        return endTime - startTime;
    }

    public long getPreCommitTimeNanos()
    {
        return preCommitTime.get();
    }

    public long getInCommitTimeNanos()
    {
        return inCommitTime.get();
    }

    public OperationCounter txBegin( Transaction transaction )
    {
        transactionTimeHolder timeHolder = new transactionTimeHolder();
        timeHolder.start = System.nanoTime();
        transactionTimes.put( transaction, timeHolder );
        return this;
    }

    public OperationCounter txBeforeFinish( Transaction transaction )
    {
        transactionTimeHolder timeHolder = transactionTimes.get( transaction );
        timeHolder.middle = System.nanoTime();
        long time = preCommitTime.get();
        while ( !preCommitTime.compareAndSet( time, time
            + (timeHolder.middle - timeHolder.start) ) )
        {
            time = preCommitTime.get();
        }
        return this;
    }

    public OperationCounter txEnd( Transaction transaction )
    {
        transactionTimeHolder timeHolder = transactionTimes.get( transaction );
        timeHolder.end = System.nanoTime();
        long time = inCommitTime.get();
        while ( !inCommitTime.compareAndSet( time, time
            + (timeHolder.end - timeHolder.middle) ) )
        {
            time = inCommitTime.get();
        }
        return this;
    }

    public long getNumberOfOperations( Operation operation )
    {
        switch ( operation )
        {
            case NEO_GET_REFERENCE_NODE:
                return neoGetReferenceNodeCounter.longValue();
            case NEO_GET_NODE_BY_ID:
                return neoGetNodeByIdCounter.longValue();
            case NEO_GET_RELATIONSHIP_BY_ID:
                return neoGetRelationshipByIdCounter.longValue();
            case RELATIONSHIP_CREATE:
                return relationshipCreateCounter.longValue();
            case RELATIONSHIP_DELETE:
                return relationshipDeleteCounter.longValue();
            case RELATIONSHIP_GET_START_NODE:
                return relationshipGetStartNodeCounter.longValue();
            case RELATIONSHIP_GET_END_NODE:
                return relationshipGetEndNodeCounter.longValue();
            case RELATIONSHIP_GET_OTHER_NODE:
                return relationshipGetOtherNodeCounter.longValue();
            case RELATIONSHIP_GET_NODES:
                return relationshipGetNodesCounter.longValue();
            case NODE_CREATE:
                return nodeCreateCounter.longValue();
            case NODE_DELETE:
                return nodeDeleteCounter.longValue();
            case NODE_GET_RELATIONSHIPS:
                return nodeGetRelationshipsCounter.longValue();
            case NODE_HAS_RELATIONSHIP:
                return nodeHasRelationshipCounter.longValue();
            case NODE_GET_SINGLE_RELATIONSHIP:
                return nodeGetSingleRelationshipCounter.longValue();
            case PROPERTYCONTAINER_HAS_PROPERTY:
                return PropertyContainerHasPropertyCounter.longValue();
            case PROPERTYCONTAINER_GET_PROPERTY:
                return PropertyContainerGetPropertyCounter.longValue();
            case PROPERTYCONTAINER_SET_PROPERTY:
                return PropertyContainerSetPropertyCounter.longValue();
            case PROPERTYCONTAINER_REMOVE_PROPERTY:
                return PropertyContainerRemovePropertyCounter.longValue();
            case PROPERTYCONTAINER_GET_PROPERTY_KEYS:
                return PropertyContainerGetPropertyKeysCounter.longValue();
            case PROPERTYCONTAINER_GET_PROPERTY_VALUES:
                return PropertyContainerGetPropertyValuesCounter.longValue();
        }
        return 0;
    }

    public List<Operation> getNonZeroOperations()
    {
        List<Operation> result = new LinkedList<Operation>();
        for ( Operation operation : OperationCounter.Operation.values() )
        {
            if ( getNumberOfOperations( operation ) > 0 )
            {
                result.add( operation );
            }
        }
        return result;
    }

    public float getOperationWeight( Operation operation )
    {
        Float value = operationWeights.get( operation );
        if ( value == null )
        {
            return OperationCounter.standardOperationWeight;
        }
        return value;
    }

    public void setOperationWeight( Operation operation, float weight )
    {
        operationWeights.put( operation, weight );
    }

    public long getTotalWeightForAllOperations()
    {
        long sum = 0;
        for ( Operation operation : OperationCounter.Operation.values() )
        {
            sum += getNumberOfOperations( operation )
                * getOperationWeight( operation );
        }
        return sum;
    }

    @Override
    public String toString()
    {
        String result = "";
        for ( Operation operation : getNonZeroOperations() )
        {
            if ( result.length() > 0 )
            {
                result += ", ";
            }
            result += getNumberOfOperations( operation ) + " " + operation;
        }
        return result;
    }

    public double getScore()
    {
        // Seconds
        double timeScore = getTotalTimeNanos() / 1000000000.0;
        // Make sure it is not zero.
        if ( timeScore < 0.000000001 )
        {
            timeScore = 0.000000001;
        }
        return (double) getTotalWeightForAllOperations() / timeScore;
    }

    // Here follows the reporting methods.
    public OperationCounter neoGetReferenceNode()
    {
        neoGetReferenceNodeCounter.incrementAndGet();
        return this;
    }

    public OperationCounter neoGetNodeById()
    {
        neoGetNodeByIdCounter.incrementAndGet();
        return this;
    }

    public OperationCounter neoGetRelationshipById()
    {
        neoGetRelationshipByIdCounter.incrementAndGet();
        return this;
    }

    public OperationCounter relationshipCreate()
    {
        relationshipCreateCounter.incrementAndGet();
        return this;
    }

    public OperationCounter relationshipDelete()
    {
        relationshipDeleteCounter.incrementAndGet();
        return this;
    }

    public OperationCounter relationshipGetStartNode()
    {
        relationshipGetStartNodeCounter.incrementAndGet();
        return this;
    }

    public OperationCounter relationshipGetEndNode()
    {
        relationshipGetEndNodeCounter.incrementAndGet();
        return this;
    }

    public OperationCounter relationshipGetOtherNode()
    {
        relationshipGetOtherNodeCounter.incrementAndGet();
        return this;
    }

    public OperationCounter relationshipGetNodes()
    {
        relationshipGetNodesCounter.incrementAndGet();
        return this;
    }

    public OperationCounter nodeCreate()
    {
        nodeCreateCounter.incrementAndGet();
        return this;
    }

    public OperationCounter nodeDelete()
    {
        nodeDeleteCounter.incrementAndGet();
        return this;
    }

    public OperationCounter nodeGetRelationships()
    {
        nodeGetRelationshipsCounter.incrementAndGet();
        return this;
    }

    public OperationCounter nodeHasRelationship()
    {
        nodeHasRelationshipCounter.incrementAndGet();
        return this;
    }

    public OperationCounter nodeGetSingleRelationship()
    {
        nodeGetSingleRelationshipCounter.incrementAndGet();
        return this;
    }

    public OperationCounter PropertyContainerHasProperty()
    {
        PropertyContainerHasPropertyCounter.incrementAndGet();
        return this;
    }

    public OperationCounter PropertyContainerGetProperty()
    {
        PropertyContainerGetPropertyCounter.incrementAndGet();
        return this;
    }

    public OperationCounter PropertyContainerSetProperty()
    {
        PropertyContainerSetPropertyCounter.incrementAndGet();
        return this;
    }

    public OperationCounter PropertyContainerRemoveProperty()
    {
        PropertyContainerRemovePropertyCounter.incrementAndGet();
        return this;
    }

    public OperationCounter PropertyContainerGetPropertyKeys()
    {
        PropertyContainerGetPropertyKeysCounter.incrementAndGet();
        return this;
    }

    public OperationCounter PropertyContainerGetPropertyValues()
    {
        PropertyContainerGetPropertyValuesCounter.incrementAndGet();
        return this;
    }
}
