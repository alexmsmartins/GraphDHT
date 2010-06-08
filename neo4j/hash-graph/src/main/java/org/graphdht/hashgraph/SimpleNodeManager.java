/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.util.*;

import org.graphdht.hashcontainer.HTServiceFactory;
import org.graphdht.hashcontainer.SimpleHT;
import org.graphdht.hashcontainer.SimpleHTServiceFactory;
import org.graphdht.openchord.OpenChordHTServiceFactory;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.transaction.TransactionFailureException;

import javax.transaction.TransactionManager;

import org.graphdht.dht.HTService;


/**
 * @author alex
 */
public class SimpleNodeManager {

    HTServiceFactory fact = null;
    HTService<Long,PropertyContainer> nodeAndRelMap;
    //SimpleHT<Node> nodeMap;
    //SimpleHT<Relationship> relationshipMap;

    SimpleNodeManager() {
        nodeAndRelMap = new SimpleHT<Long,PropertyContainer>();
        //nodeMap = new SimpleHT<Node>();
        //relationshipMap = new SimpleHT<Relationship>();
        //create reference node
        Node referenceNode = new SimpleNode(0L, this);
        //nodeMap.put(referenceNode.getId(), referenceNode );
        nodeAndRelMap.put(referenceNode.getId(), referenceNode );
    }

    public SimpleNodeManager(String config) {
        if(config == "simple")
            fact = new SimpleHTServiceFactory<PropertyContainer>();
        else if(config == "openchord")
            fact = new OpenChordHTServiceFactory<SimplePropertyContainer>();
        else
            throw new RuntimeException("Option for HTService not avilable");

        nodeAndRelMap = fact.createHTService();
        //nodeMap = new SimpleHT<Node>();
        //relationshipMap = new SimpleHT<Relationship>();
        //create reference node
        Node referenceNode = new SimpleNode(0L, this);
        //nodeMap.put(referenceNode.getId(), referenceNode );
        nodeAndRelMap.put(referenceNode.getId(), referenceNode );
    }

    public Node createNode() {
        Node node = null;
        node = new SimpleNode(generateNextId(), this);
        //this.nodeMap.put(node.getId(), node);
        this.nodeAndRelMap.put(node.getId(), node);
        return node;
    }

    public Node getNodeById(long id) {
        //return this.nodeMap.get(id);
        return (Node)this.nodeAndRelMap.get(id);
    }

    public Relationship getRelationshipById(long id) {
        //return this.relationshipMap.get(id);
        return (Relationship)this.nodeAndRelMap.get(id);
    }

    public Node getReferenceNode() {
        //return this.nodeMap.get(new Long(0));
        return (Node)this.nodeAndRelMap.get(new Long(0));
    }

    /**
     * Lists all the nodes that belong to the graph.
     * This method should be avoided at all costs since it may cause OutOfMemoryError
     * @return
     */
    public Iterable<Node> getAllNodes() {
        //return this.nodeMap.getAllValues();
        Collection<Node> nodes = new ArrayList<Node>();
        for(PropertyContainer node: this.nodeAndRelMap.getAllValues()){

            if( node.getClass().equals(SimpleNode.class) ){
                nodes.add( (Node)node);
            }
        }
        return nodes;
    }

    public Iterable<RelationshipType> getRelationshipTypes() {
        /*Map<RelationshipType, Integer> m = new HashMap<RelationshipType, Integer>();
        for (Relationship e : this.relationshipMap.getAllValues()) {
            if (!m.containsKey(e.getType())) {
                m.put(e.getType(), 0);
            }
        }
        return m.keySet();*/
        Map<RelationshipType, Integer> m = new HashMap<RelationshipType, Integer>();
        for (PropertyContainer e : this.nodeAndRelMap.getAllValues()) {
            if( e.getClass() == Relationship.class ){
                if (!m.containsKey(((Relationship)e).getType())) {
                    m.put( ((Relationship)e).getType(), 0);
                }
            }
        }
        return m.keySet();
    }

    public void shutdown() {
        //TODO Does it make sense to shutdown Chord here?
        return;
    }


    //methods that should not be implemented for remote DBs

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

    //methods that should not be implemented for remote DBs

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
        //isolated nodes should also be deleted
        //Relationship rel = this.relationshipMap.get(aLong);
        Relationship rel = (Relationship)this.nodeAndRelMap.get(aLong);
        SimpleNode startNode = (SimpleNode) this.getNodeById(rel.getStartNode().getId());
        SimpleNode endNode = (SimpleNode) this.getNodeById(rel.getEndNode().getId());
        startNode.deleteRelationship(aLong);
        endNode.deleteRelationship(aLong);
        //this.relationshipMap.remove(aLong);
        this.nodeAndRelMap.remove(aLong);
    }

    public Relationship createRelationship(long simpleNodeId, long otherNodeId, RelationshipType type) {
        //SimpleNode otherNode = (SimpleNode)this.nodeMap.get(otherNodeId);
        SimpleNode otherNode = (SimpleNode)this.nodeAndRelMap.get(otherNodeId);
        if (otherNode != null) {
            Relationship rel = new SimpleRelationship(generateNextId(), simpleNodeId, otherNodeId, type, true, this);
            //this.relationshipMap.put(rel.getId(), rel);
            this.nodeAndRelMap.put(rel.getId(), rel);
            otherNode.addRelationship(rel);
            return rel;
        } else {
            throw new org.neo4j.graphdb.NotFoundException("The destiny node of this relationship does not exist in the graph");
        }
    }

    public Node deleteNode(Long aLong) {
        //return this.nodeMap.remove(aLong);
        Node node = (Node)this.nodeAndRelMap.get(aLong); //confirms if this is really a node
        return (Node)this.nodeAndRelMap.remove(aLong);
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
