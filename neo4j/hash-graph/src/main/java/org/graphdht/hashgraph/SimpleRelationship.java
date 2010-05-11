/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author alex
 */
public class SimpleRelationship implements Relationship, Serializable {

    public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getStartNode() {
        throw new UnsupportedOperationException("Not supported yet.");
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
