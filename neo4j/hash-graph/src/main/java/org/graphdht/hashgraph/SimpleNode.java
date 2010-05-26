/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.graphdht.dht.rmi.DHTService;
import org.graphdht.hashcontainer.SimpleDHT;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

/**
 *
 * @author alex
 */
public class SimpleNode extends SimplePrimitive implements Node, Serializable {

    List<Relationship> relationships = new ArrayList();

    public SimpleNode(long id, SimpleDHT<Node> service){
        super(id, service);
    }

    public long getId() {
        return id;
    }

    public void delete(){
        try {
            dht.remove(
                    new Long(this.getId())
            );
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Iterable<Relationship> getRelationships() {
        return this.relationships;
    }

    public boolean hasRelationship() {
        return relationships.size() > 0?true:false;
    }

    public Iterable<Relationship> getRelationships(RelationshipType... types) {
        List<Relationship> r = new LinkedList();
        for (Relationship rel : this.relationships ){
            for(RelationshipType relType : types ){
                if(rel.getType() == relType){
                    r.add(rel);
                }
            }
        }
        return r;
    }

    public boolean hasRelationship(RelationshipType... types) {
        List<Relationship> r = new LinkedList();
        for (Relationship rel : this.relationships ){
            for(RelationshipType relType : types ){
                if(rel.getType() == relType){
                    return true;
                }
            }
        }
        return false;
    }

    public Iterable<Relationship> getRelationships(Direction dir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasRelationship(Direction dir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<Relationship> getRelationships(RelationshipType type, Direction dir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasRelationship(RelationshipType type, Direction dir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Relationship getSingleRelationship(RelationshipType type, Direction dir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Relationship createRelationshipTo(Node otherNode, RelationshipType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Traverser traverse(Order traversalOrder, StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator, RelationshipType relationshipType, Direction direction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Traverser traverse(Order traversalOrder, StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator, RelationshipType firstRelationshipType, Direction firstDirection, RelationshipType secondRelationshipType, Direction secondDirection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Traverser traverse(Order traversalOrder, StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator, Object... relationshipTypesAndDirections) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Deprecated //cast from long to int might cause probles
    public int hashCode(){
        return (int)this.getId();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleNode other = (SimpleNode) obj;
        if(this.getId() == other.getId())
            return true;
        else
            return false;
    }
}
