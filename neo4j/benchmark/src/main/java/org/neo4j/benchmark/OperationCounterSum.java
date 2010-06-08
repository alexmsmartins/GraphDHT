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

import java.util.LinkedList;
import java.util.List;

import org.neo4j.api.core.Transaction;

/**
 * A utility that can be used to automatically summarize the values of several
 * {@link OperationCounter}.
 * @author Patrik
 */
public class OperationCounterSum implements OperationCounter
{
    List<OperationCounter> counters = new LinkedList<OperationCounter>();

    /**
     * Add an {@link OperationCounter} to the collection.
     * @param operationCounter
     */
    public void addCounter( OperationCounter operationCounter )
    {
        counters.add( operationCounter );
    }

    /**
     * Remove an {@link OperationCounter} from the collection.
     * @param operationCounter
     */
    public void removeCounter( OperationCounter operationCounter )
    {
        counters.remove( operationCounter );
    }

    public long getNumberOfOperations( Operation operation )
    {
        long sum = 0;
        for ( OperationCounter operationCounter : counters )
        {
            sum += operationCounter.getNumberOfOperations( operation );
        }
        return sum;
    }

    public float getOperationWeight( Operation operation )
    {
        throw new RuntimeException(
            "Getting operation weight for OperationCounterSum makes no sense." );
    }

    /**
     * This modifies the operation weight for an operation in all
     * {@link OperationCounter}s in the collection.
     */
    public void setOperationWeight( Operation operation, float weight )
    {
        for ( OperationCounter operationCounter : counters )
        {
            operationCounter.setOperationWeight( operation, weight );
        }
    }

    public long getTotalWeightForAllOperations()
    {
        long sum = 0;
        for ( OperationCounter operationCounter : counters )
        {
            sum += operationCounter.getTotalWeightForAllOperations();
        }
        return sum;
    }

    /**
     * Returns the average score from all the {@link OperationCounter}s.
     */
    public double getScore()
    {
        // Returns the average value
        double result = 0;
        for ( OperationCounter operationCounter : counters )
        {
            result += operationCounter.getScore();
        }
        result /= counters.size();
        return result;
    }

    public long getPreCommitTimeNanos()
    {
        long sum = 0;
        for ( OperationCounter operationCounter : counters )
        {
            sum += operationCounter.getPreCommitTimeNanos();
        }
        return sum;
    }

    public long getTotalTimeNanos()
    {
        long sum = 0;
        for ( OperationCounter operationCounter : counters )
        {
            sum += operationCounter.getTotalTimeNanos();
        }
        return sum;
    }

    public long getInCommitTimeNanos()
    {
        long sum = 0;
        for ( OperationCounter operationCounter : counters )
        {
            sum += operationCounter.getInCommitTimeNanos();
        }
        return sum;
    }

    protected void generateOperationReportError()
    {
        throw new RuntimeException(
            "Tried to report events to an OperationCounterSum object." );
    }

    public OperationCounter PropertyContainerGetProperty()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter PropertyContainerGetPropertyKeys()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter PropertyContainerGetPropertyValues()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter PropertyContainerHasProperty()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter PropertyContainerRemoveProperty()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter PropertyContainerSetProperty()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter neoGetNodeById()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter neoGetReferenceNode()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter neoGetRelationshipById()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter nodeCreate()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter nodeDelete()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter nodeGetRelationships()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter nodeGetSingleRelationship()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter nodeHasRelationship()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter relationshipCreate()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter relationshipDelete()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter relationshipGetEndNode()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter relationshipGetNodes()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter relationshipGetOtherNode()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter relationshipGetStartNode()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter timeBegin()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter timeEnd()
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter txBeforeFinish( Transaction transaction )
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter txBegin( Transaction transaction )
    {
        generateOperationReportError();
        return null;
    }

    public OperationCounter txEnd( Transaction transaction )
    {
        generateOperationReportError();
        return null;
    }
}
