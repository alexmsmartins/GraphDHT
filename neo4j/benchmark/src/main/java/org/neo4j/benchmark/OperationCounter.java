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

import org.neo4j.api.core.Transaction;

/**
 * The OperationCounter is a utility that can be used to keep track of how many
 * operations in the Neo4j core API are performed during a benchmark/test. These
 * operations must be manually reported to this utility, as well as starting
 * time and stopping time of the benchmark/test, and transaction activity, all
 * depending on what data is interesting. This utility can then calculate a
 * score from the number of operations and time.
 * @author Patrik
 */
public interface OperationCounter
{
    /**
     * Enum defining the various operations that can be counted.
     */
    public static enum Operation
    {
        NEO_GET_REFERENCE_NODE, NEO_GET_NODE_BY_ID, NEO_GET_RELATIONSHIP_BY_ID,
        RELATIONSHIP_CREATE, RELATIONSHIP_DELETE, RELATIONSHIP_GET_START_NODE,
        RELATIONSHIP_GET_END_NODE, RELATIONSHIP_GET_OTHER_NODE,
        RELATIONSHIP_GET_NODES, NODE_CREATE, NODE_DELETE,
        NODE_GET_RELATIONSHIPS, NODE_HAS_RELATIONSHIP,
        NODE_GET_SINGLE_RELATIONSHIP, PROPERTYCONTAINER_HAS_PROPERTY,
        PROPERTYCONTAINER_GET_PROPERTY, PROPERTYCONTAINER_SET_PROPERTY,
        PROPERTYCONTAINER_REMOVE_PROPERTY, PROPERTYCONTAINER_GET_PROPERTY_KEYS,
        PROPERTYCONTAINER_GET_PROPERTY_VALUES,
    }

    static final float standardOperationWeight = 1;

    /**
     * @return The sum of all operations multiplied by their weight.
     */
    public long getTotalWeightForAllOperations();

    /**
     * @return The final score for this counter.
     */
    public double getScore();

    /**
     * @param operation
     * @return The number of reported operations of a certain type.
     */
    public long getNumberOfOperations( Operation operation );

    /**
     * @param operation
     * @return The weight used for a certain operation when calulating the total
     *         weight and score.
     */
    public float getOperationWeight( Operation operation );

    /**
     * Alters the weight used for a certain operation when calulating the total
     * weight and score.
     * @param operation
     * @param weight
     */
    public void setOperationWeight( Operation operation, float weight );

    /**
     * Signals that the testing is starting.
     * @return Itself.
     */
    OperationCounter timeBegin();

    /**
     * Signals that the testing is ending.
     * @return Itself.
     */
    OperationCounter timeEnd();

    /**
     * @return The elapsed time between the timeBegin and timeEnd calls in
     *         nanoseconds.
     */
    long getTotalTimeNanos();

    /**
     * @return The total time transactions spent before committing in
     *         nanoseconds.
     */
    long getPreCommitTimeNanos();

    /**
     * @return The total time transactions spent with committing in nanoseconds.
     */
    long getInCommitTimeNanos();

    /**
     * Reports that a transaction will begin.
     * @param transaction
     * @return Itself.
     */
    OperationCounter txBegin( Transaction transaction );

    /**
     * Reports that a transaction will now finish. This means that time will
     * switch from being added to the preCommit time to being added to the
     * inCommit time.
     * @param transaction
     * @return Itself.
     */
    OperationCounter txBeforeFinish( Transaction transaction );

    /**
     * Reports that a transaction has finished.
     * @param transaction
     * @return Itself.
     */
    OperationCounter txEnd( Transaction transaction );

    // The various reporting methods follows
    public OperationCounter neoGetReferenceNode();

    public OperationCounter neoGetNodeById();

    public OperationCounter neoGetRelationshipById();

    public OperationCounter relationshipCreate();

    public OperationCounter relationshipDelete();

    public OperationCounter relationshipGetStartNode();

    public OperationCounter relationshipGetEndNode();

    public OperationCounter relationshipGetOtherNode();

    public OperationCounter relationshipGetNodes();

    public OperationCounter nodeCreate();

    public OperationCounter nodeDelete();

    public OperationCounter nodeGetRelationships();

    public OperationCounter nodeHasRelationship();

    public OperationCounter nodeGetSingleRelationship();

    public OperationCounter PropertyContainerHasProperty();

    public OperationCounter PropertyContainerGetProperty();

    public OperationCounter PropertyContainerSetProperty();

    public OperationCounter PropertyContainerRemoveProperty();

    public OperationCounter PropertyContainerGetPropertyKeys();

    public OperationCounter PropertyContainerGetPropertyValues();
}
