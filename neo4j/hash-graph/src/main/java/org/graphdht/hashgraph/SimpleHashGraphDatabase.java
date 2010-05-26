/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.rmi.RemoteException;
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
public class SimpleHashGraphDatabase implements GraphDatabaseService {

    //FIXME Lets try to write the code here directly but later we have to change it
    SimpleDHT<Node> nodeMap;
    SimpleDHT<Relationship> relationshipMap;

    SimpleHashGraphDatabase(String string) {
        nodeMap = new SimpleDHT<Node>();
        relationshipMap = new SimpleDHT<Relationship>();
    }

    public Node createNode() {
        Node node = null;
        try {
            node = new SimpleNode(generateNextId(), this.nodeMap);
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
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<RelationshipType> getRelationshipTypes() {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void shutdown() {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
    }


    //these methods should not be implemented for know

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
