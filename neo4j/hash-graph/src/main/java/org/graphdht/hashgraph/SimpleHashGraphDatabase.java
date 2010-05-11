/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;



/**
 *
 * @author alex
 */
public class SimpleHashGraphDatabase implements GraphDatabaseService {

    //FIXME Lets try to write the code here directly but later we have to change it
    ConcurrentHashMap<Long, Node> nodeMap;
    ConcurrentHashMap<Long, Relationship> relationshipMap;


    SimpleHashGraphDatabase(String string) {
        nodeMap = new ConcurrentHashMap<Long, Node>();
        relationshipMap = new ConcurrentHashMap<Long, Relationship>();
    }

    public Node createNode() {
        Node node = new SimpleNode();
        //TODO id generation code
        return node;

    }

    public Node getNodeById(long id) {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Relationship getRelationshipById(long id) {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getReferenceNode() {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
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

    public boolean enableRemoteShell() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean enableRemoteShell(Map<String, Serializable> initialProperties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Transaction beginTx() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
