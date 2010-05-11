/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
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
public class SimpleNode implements Node, Serializable {

    public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<Relationship> getRelationships() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasRelationship() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<Relationship> getRelationships(RelationshipType... types) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasRelationship(RelationshipType... types) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public boolean hasProperty(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getProperty(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getProperty(String key, Object defaultValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setProperty(String key, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object removeProperty(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<String> getPropertyKeys() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<Object> getPropertyValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
