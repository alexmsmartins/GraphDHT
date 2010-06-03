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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.transaction.TransactionFailureException;

import javax.transaction.TransactionManager;


/**
 *
 * @author alex
 */
public class SimpleNodeManager {

    //FIXME Lets try to write the code here directly but later we have to change it
    SimpleDHT<Node> nodeMap;
    SimpleDHT<Relationship> relationshipMap;

    SimpleNodeManager() {
        nodeMap = new SimpleDHT<Node>();
        relationshipMap = new SimpleDHT<Relationship>();
    }

    public Node createNode() {
        Node node = null;
        try {
            node = new SimpleNode(generateNextId(), this);
        } catch (OverFlowException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
        return node;

    }

    public Node getNodeById(long id) {
        try {
            return this.nodeMap.get(id);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Relationship getRelationshipById(long id) {
        try {
            return this.relationshipMap.get(id);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Node getReferenceNode() {
        try {
            return this.nodeMap.get(new Long(Long.MIN_VALUE));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Iterable<Node> getAllNodes() {
        return this.nodeMap.getAllValues();
    }

    public Iterable<RelationshipType> getRelationshipTypes() {
        Map<RelationshipType, Integer> m = new HashMap<RelationshipType, Integer>();
        for(Relationship e: this.relationshipMap.getAllValues()){
            if( !m.containsKey(e.getType() ) ){
                m.put(e.getType(), 0);
            }
        }
        return m.keySet();
    }

    public void shutdown() {
        //TODO perhaps it makes sense to shutdown Chord here
        return ;
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
    static long nextId = 0;
    static boolean thereIsNextId = true;

    private static long generateNextId() throws OverFlowException{
        if(thereIsNextId == false){
            throw new OverFlowException();
        }
        if(nextId == Long.MAX_VALUE){
            thereIsNextId = false; //last value guaranteed to be unique
        }
        return nextId++;
    }

    public void deleteRelationship(Long aLong) throws RemoteException {
        //TODO check if isolated nodes should also be deleted
        Relationship rel = this.relationshipMap.get(aLong);
        SimpleNode startNode = (SimpleNode)this.getNodeById(rel.getStartNode().getId());
        SimpleNode endNode = (SimpleNode)this.getNodeById(rel.getEndNode().getId());
        startNode.deleteRelationship(aLong);
        endNode.deleteRelationship(aLong);
        this.relationshipMap.remove(aLong);
    }

    public Relationship createRelationship(long simpleNodeId, long otherNodeId, RelationshipType type) throws OverFlowException, RemoteException {
        long relId = generateNextId();
        Node otherNode = this.nodeMap.get(otherNodeId);
        //TODO ckeck if we should create a new node or not 
        if(otherNode != null) {
            System.out.print("Aqui!!");
            Relationship rel = new SimpleRelationship(relId, simpleNodeId, otherNodeId, type, true, this);
            return this.relationshipMap.put(relId, rel);
        } else {
            System.out.print("Ali!!");
            return null;
        }
    }

    public Node deleteNode(Long aLong) {

        try {
            return this.nodeMap.remove(aLong);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    /**
     * This class should be kept like this
     * Since this is an implementation of Neo4J over Chord two different aspects must be balanced
     *  - Neo4J transaction tests must pass
     *  - Chord is non transactional so real transactions should not occur
     */
    private static class PlaceboTransaction implements Transaction
    {
        private final TransactionManager transactionManager;

        PlaceboTransaction( TransactionManager transactionManager )
        {
            // we should override all so null is ok
            this.transactionManager = transactionManager;
        }

        public void failure()
        {
            try
            {
                transactionManager.getTransaction().setRollbackOnly();
            }
            catch ( Exception e )
            {
                throw new TransactionFailureException(
                    "Failed to mark transaction as rollback only.", e );
            }
        }

        public void success()
        {
        }

        public void finish()
        {
        }
    }

}
