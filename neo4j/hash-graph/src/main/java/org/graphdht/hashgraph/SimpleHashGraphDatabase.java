package org.graphdht.hashgraph;

import org.neo4j.graphdb.*;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/Jun/2010
 * Time: 7:29:50
 * To change this template use File | Settings | File Templates.
 */
public class SimpleHashGraphDatabase implements GraphDatabaseService {

    SimpleNodeManager nodeManager;

    public  SimpleHashGraphDatabase(String string ) {
        nodeManager = new SimpleNodeManager();
    }

    public Node createNode() {
        return this.nodeManager.createNode();
    }

    public Node getNodeById(long id) {
        return this.nodeManager.getNodeById(id);
    }

    public Relationship getRelationshipById(long id) {
        return this.nodeManager.getRelationshipById(id);
    }

    public Node getReferenceNode() {
        return this.nodeManager.getReferenceNode();
    }

    public Iterable<Node> getAllNodes() {
        return this.nodeManager.getAllNodes();
    }

    public Iterable<RelationshipType> getRelationshipTypes() {
        return this.nodeManager.getRelationshipTypes();
    }

    public void shutdown() {
        this.nodeManager.shutdown();
    }

    public boolean enableRemoteShell() {
        return this.nodeManager.enableRemoteShell();
    }

    public boolean enableRemoteShell(Map<String, Serializable> initialProperties) {
        return this.nodeManager.enableRemoteShell(initialProperties);
    }

    public Transaction beginTx() {
        return this.nodeManager.beginTx();
    }
}
