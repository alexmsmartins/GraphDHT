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
 *
 * @author alex
 */
public class SimpleRelationship extends SimplePrimitive implements Relationship, Serializable {

    //TODO - WRONG BUT SHOULD CONTINUE HERE
    long startNode;
    long endNode;

    public SimpleRelationship(long id, DHTService<Long, SimpleRelationship> service){
        super(id, service);
    }


    public long getId() {
        return id;
    }

    public void delete() {
        try {
            dht.remove(
                    new Long(this.getId())
            );
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }        
    }

    public Node getStartNode() {
        try {
            return (Node) dht.get( new Long(Long.MIN_VALUE) );
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        };
        return null;
    }

    public Node getEndNode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getOtherNode(Node node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node[] getNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RelationshipType getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isType(RelationshipType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
