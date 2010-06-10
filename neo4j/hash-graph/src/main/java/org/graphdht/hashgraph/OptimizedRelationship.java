/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * @author alex
 */
public class OptimizedRelationship extends OptimizedPrimitive implements Relationship, Serializable {

    private final long nodeId[] = new long[2];
    private final RelationshipType type;

    // Dummy constructor for NodeManager to acquire read lock on relationship
    // when loading from PL.

    OptimizedRelationship(long id, OptimizedNodeManager service) throws RemoteException {
        super(id, service);
        Relationship tmp = (Relationship) service.getRelationshipById(id);
        if (tmp != null) {
            this.nodeId[0] = tmp.getStartNode().getId();
            this.nodeId[1] = tmp.getEndNode().getId();
            this.type = tmp.getType();
        } else {
            this.nodeId[0] = -1;
            this.nodeId[1] = -1;
            this.type = null;
        }
    }

    OptimizedRelationship(long id, long startNodeId, long endNodeId,
                       RelationshipType type, boolean newRel, OptimizedNodeManager service) {
        super(id, service);
        if (type == null) {
            throw new IllegalArgumentException("Null type");
        }
        if (startNodeId == endNodeId) {
            throw new IllegalArgumentException("Start node equals end node");
        }

        this.nodeId[0] = startNodeId;
        this.nodeId[1] = endNodeId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void delete() {
        dhtService.deleteRelationship(this.getId());
    }

    public Node getStartNode() {
        return (Node) dhtService.getNodeById(this.nodeId[0]);
    }

    public Node getEndNode() {
        return (Node) dhtService.getNodeById(this.nodeId[1]);
    }

    public long getStartNodeId() {
        return this.nodeId[0];
    }

    public long getEndNodeId() {
        return this.nodeId[1];
    }


    public Node getOtherNode(Node node) {
        if (node.getId() == this.nodeId[0]) {
            return this.getEndNode();
        } else if (node.getId() == this.nodeId[1]) {
            return this.getEndNode();
        } else {
            return null;
        }
    }

    public Node[] getNodes() {
        return new Node[]{
                this.dhtService.getNodeById(this.nodeId[0]),
                this.dhtService.getNodeById(this.nodeId[1])};
    }

    public RelationshipType getType() {
        return this.type;
    }

    public boolean isType(RelationshipType type) {
        return this.type == type;
    }
}