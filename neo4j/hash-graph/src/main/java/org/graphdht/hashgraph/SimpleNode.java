/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

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
 * @author alex
 */
public class SimpleNode extends SimplePrimitive implements Node, Serializable {

    /**
     * Lists the <code>Relationship</code>s associated to this <code>Node</code>.
     * Both Direction.OUTGOING AND Direction.INCOMING are included.
     */
    List<Relationship> relationships = new ArrayList();

    /**
     * Defines the <code>direction</code> of the <code>Relationship</code> in the same position.
     */
    List<Direction> relDirection = new ArrayList();

    public SimpleNode(long id, SimpleNodeManager service) {
        super(id, service);
    }

    public long getId() {
        return id;
    }

    public void delete() {
        this.dhtService.deleteNode(
                new Long(this.getId())
        );
    }

    public Iterable<Relationship> getRelationships() {
        return this.relationships;
    }

    public boolean hasRelationship() {
        return relationships.size() > 0 ? true : false;
    }

    public Iterable<Relationship> getRelationships(RelationshipType... types) {
        List<Relationship> r = new LinkedList();
        for (Relationship rel : this.relationships) {
            for (RelationshipType relType : types) {
                if (rel.getType() == relType) {
                    r.add(rel);
                }
            }
        }
        return r;
    }

    public boolean hasRelationship(RelationshipType... types) {
        List<Relationship> r = new LinkedList();
        for (Relationship rel : this.relationships) {
            for (RelationshipType relType : types) {
                if (rel.getType() == relType) {
                    return true;
                }
            }
        }
        return false;
    }

    public Iterable<Relationship> getRelationships(Direction dir) {
        Collection<Relationship> c;
        if (dir == Direction.BOTH)
            return this.getRelationships();
        else {
            c = new ArrayList<Relationship>();
            if (this.relationships.size() > 0) {
                Iterator<Relationship> relIt = this.relationships.iterator();
                Iterator<Direction> relDirIt = this.relDirection.iterator();
                Direction d;
                Relationship rel;
                for (rel = relIt.next(), d = relDirIt.next(); relIt.hasNext();) {
                    if (d == dir) {
                        c.add(rel);
                    }
                }
            }
            return c;
        }
    }

    public boolean hasRelationship(Direction dir) {
        if (this.relationships.size() > 0) {
            if (dir == Direction.BOTH)
                return true;
            else {
                Iterator<Relationship> relIt = this.relationships.iterator();
                Iterator<Direction> relDirIt = this.relDirection.iterator();
                Direction d;
                Relationship rel;
                for (rel = relIt.next(), d = relDirIt.next(); relIt.hasNext();) {
                    if (d == dir) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Iterable<Relationship> getRelationships(RelationshipType type, Direction dir) {
        Collection<Relationship> c = new ArrayList<Relationship>();
        if (this.relationships.size() > 0) {
            Iterator<Relationship> relIt = this.relationships.iterator();
            Iterator<Direction> relDirIt = this.relDirection.iterator();
            Direction d;
            Relationship rel;
            for (rel = relIt.next(), d = relDirIt.next(); relIt.hasNext();) {
                if ((dir == Direction.BOTH || d == dir) && rel.isType(type)) {
                    c.add(rel);
                }
            }
        }
        return c;
    }

    public boolean hasRelationship(RelationshipType type, Direction dir) {
        if (this.relationships.size() > 0) {
            Iterator<Relationship> relIt = this.relationships.iterator();
            Iterator<Direction> relDirIt = this.relDirection.iterator();
            Direction d;
            Relationship rel;
            for (rel = relIt.next(), d = relDirIt.next(); relIt.hasNext();) {
                if ((dir == Direction.BOTH || d == dir) && rel.isType(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Relationship getSingleRelationship(RelationshipType type, Direction dir) {
        if (this.relationships.size() > 0) {
            Iterator<Relationship> relIt = this.relationships.iterator();
            Iterator<Direction> relDirIt = this.relDirection.iterator();
            Direction d;
            Relationship rel;
            for (rel = relIt.next(), d = relDirIt.next(); relIt.hasNext();) {
                if ((dir == Direction.BOTH || d == dir) && rel.isType(type)) {
                    return rel;
                }
            }
        }
        return null;
    }

    public Relationship createRelationshipTo(Node otherNode, RelationshipType type) {
        Relationship rel;
        Direction currDir;

        //TODO this can be optimized by taking of the getRelationships(...) and doing everything in the foreach
        Iterable<Relationship> relIt = this.getRelationships(type, Direction.OUTGOING);
        //check if there are previous relationships
        for (int i = 0; i< relationships.size(); i++) {
            rel = this.relationships.get(i);
            currDir = this.relDirection.get(i);
            if (rel.getEndNode().equals(otherNode) && rel.getType() == type && currDir == Direction.OUTGOING ) {
                return rel; //returns an existing relationship instead of creating a new one
                //TODO check if returning an existing relationship is expected behaviour
            }
        }

        //create relationship
        rel = this.dhtService.createRelationship(this.id, otherNode.getId(), type);
        this.addRelationship(rel, Direction.OUTGOING );
        ((SimpleNode)otherNode).addRelationship(rel, Direction.INCOMING );
        return rel;
    }

    protected Relationship addRelationship(Relationship rel, Direction dir) {
        this.relationships.add(rel);
        this.relDirection.add(dir);
        return rel;
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

    protected Relationship deleteRelationship(long aLong) {
        for (int i = 0; i < relationships.size(); i++) {
            if (this.relationships.get(i).getId() == aLong) {
                relDirection.remove(i);
                Relationship rel = this.relationships.remove(i);
                SimpleNode node = (SimpleNode) rel.getEndNode();
                node.deleteRelationship(aLong);
                return rel;
            }
        }
        return null;
    }

    @Deprecated
    //cast from long to int might cause probles
    public int hashCode() {
        return (int) this.getId();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleNode other = (SimpleNode) obj;
        if (this.getId() == other.getId())
            return true;
        else
            return false;
    }
}
