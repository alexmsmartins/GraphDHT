/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.graphdht.dht.rmi.DHTService;
import org.graphdht.hashcontainer.SimpleDHT;
import org.graphdht.hashgraph.OverFlowException;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.transaction.TransactionFailureException;

import javax.transaction.TransactionManager;


/**
 * @author alex
 */
public class SimpleNodeManager {

    //FIXME Lets try to write the code here directly but later we have to change it
    SimpleDHT<Node> nodeMap;
    SimpleDHT<Relationship> relationshipMap;

    SimpleNodeManager() {
        nodeMap = new SimpleDHT<Node>();
        relationshipMap = new SimpleDHT<Relationship>();
        //create reference node
        Node referenceNode = new SimpleNode(0L, this);
        nodeMap.put(referenceNode.getId(), referenceNode );
    }

    public Node createNode() {
        Node node = null;
        node = new SimpleNode(generateNextId(), this);
        this.nodeMap.put(node.getId(), node);
        return node;
    }

    public Node getNodeById(long id) {
        return this.nodeMap.get(id);
    }

    public Relationship getRelationshipById(long id) {
        return this.relationshipMap.get(id);
    }

    public Node getReferenceNode() {
        return this.nodeMap.get(new Long(Long.MIN_VALUE));
    }

    public Iterable<Node> getAllNodes() {
        return this.nodeMap.getAllValues();
    }

    public Iterable<RelationshipType> getRelationshipTypes() {
        Map<RelationshipType, Integer> m = new HashMap<RelationshipType, Integer>();
        for (Relationship e : this.relationshipMap.getAllValues()) {
            if (!m.containsKey(e.getType())) {
                m.put(e.getType(), 0);
            }
        }
        return m.keySet();
    }

    public void shutdown() {
        //TODO Does it make sense to shutdown Chord here?
        return;
    }


    //these methods should not be implemented for remote DBs

    public boolean enableRemoteShell() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean enableRemoteShell(Map<String, Serializable> initialProperties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Transaction beginTx() {
        System.out.println("Stub transaction started!!");
        return new PlaceboTransaction(null);
    }

    //private methods that need to be changed or put alsewhere
    //-1 is used by Neo4J Embedded to represent an id of something that does not exist
    static long nextId = 1;
    static boolean thereIsNextId = true;

    private static long generateNextId() {
        if (thereIsNextId == false) {
            throw new OverFlowException();
        }
        if (nextId == Long.MAX_VALUE) {
            thereIsNextId = false; //last value guaranteed to be unique
        }
        return nextId++;
    }

    public void deleteRelationship(Long aLong) {
        //TODO check if isolated nodes should also be deleted
        Relationship rel = this.relationshipMap.get(aLong);
        SimpleNode startNode = (SimpleNode) this.getNodeById(rel.getStartNode().getId());
        SimpleNode endNode = (SimpleNode) this.getNodeById(rel.getEndNode().getId());
        startNode.deleteRelationship(aLong);
        endNode.deleteRelationship(aLong);
        this.relationshipMap.remove(aLong);
    }

    public Relationship createRelationship(long simpleNodeId, long otherNodeId, RelationshipType type) {
        SimpleNode otherNode = (SimpleNode)this.nodeMap.get(otherNodeId);
        if (otherNode != null) {
            Relationship rel = new SimpleRelationship(generateNextId(), simpleNodeId, otherNodeId, type, true, this);
            this.relationshipMap.put(rel.getId(), rel);
            return rel;
        } else {
            throw new org.neo4j.graphdb.NotFoundException("The destiny node of this relationship does not exist in the graph");
        }
    }

    public Node deleteNode(Long aLong) {
        return this.nodeMap.remove(aLong);
    }

    /**
     * This class should be kept like this
     * Since this is an implementation of Neo4J over Chord two different aspects must be balanced
     * - Neo4J transaction tests must pass
     * - Chord is non transactional so real transactions should not occur
     */
    private static class PlaceboTransaction implements Transaction {
        private final TransactionManager transactionManager;

        PlaceboTransaction(TransactionManager transactionManager) {
            // we should override all so null is ok
            this.transactionManager = transactionManager;
        }

        public void failure() {
            try {
                transactionManager.getTransaction().setRollbackOnly();
            } catch (Exception e) {
                throw new TransactionFailureException(
                        "Failed to mark transaction as rollback only.", e);
            }
        }

        public void success() {
        }

        public void finish() {
        }
    }
}
