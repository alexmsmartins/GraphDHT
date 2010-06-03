/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.graphdht.dht.rmi.DHTService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 * @author alex
 */
public class SimpleRelationship extends SimplePrimitive implements Relationship, Serializable {

    private final long startNodeId;
    private final long endNodeId;
    private final RelationshipType type;

    // Dummy constructor for NodeManager to acquire read lock on relationship
    // when loading from PL.

    SimpleRelationship(long id, SimpleNodeManager service) throws RemoteException {
        super(id, service);
        Relationship tmp = (Relationship) service.getRelationshipById(id);
        if (tmp != null) {
            this.startNodeId = tmp.getStartNode().getId();
            this.endNodeId = tmp.getEndNode().getId();
            this.type = tmp.getType();
        } else {
            this.startNodeId = -1;
            this.endNodeId = -1;
            this.type = null;
        }
    }

    SimpleRelationship(long id, long startNodeId, long endNodeId,
                       RelationshipType type, boolean newRel, SimpleNodeManager service) {
        super(id, service);
        if (type == null) {
            throw new IllegalArgumentException("Null type");
        }
        if (startNodeId == endNodeId) {
            throw new IllegalArgumentException("Start node equals end node");
        }

        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void delete() {
        try {
            dhtService.deleteRelationship(
                    new Long(this.getId())
            );
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Node getStartNode() {
        return (Node) dhtService.getNodeById(new Long(this.startNodeId));
    }

    public Node getEndNode() {
        return (Node) dhtService.getNodeById(new Long(this.endNodeId));
    }

    public Node getOtherNode(Node node) {
        if (node.getId() == this.startNodeId) {
            return this.getEndNode();
        } else if (node.getId() == this.endNodeId) {
            return this.getEndNode();
        } else {
            return null;
        }
    }

    public Node[] getNodes() {
        return new Node[]{this.getStartNode(), this.getEndNode()};
    }

    public RelationshipType getType() {
        return this.type;
    }

    public boolean isType(RelationshipType type) {
        return this.type == type;
    }
}
