/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphdht.hashgraph;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.kernel.impl.traversal.InternalTraverserFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author alex
 */
public class OptimizedNode extends OptimizedPrimitive implements Node, Serializable {

    /**
     * Lists the <code>Relationship</code>s associated to this <code>Node</code>.
     * Both Direction.OUTGOING AND Direction.INCOMING are included.
     */
    List<Relationship> relationships = new ArrayList();
    /**
     * Defines the <code>direction</code> of the <code>Relationship</code> in the same position.
     */
    List<Direction> relDirection = new ArrayList();
    transient InternalTraverserFactory traverserFactory = new InternalTraverserFactory();

    public OptimizedNode(long id, OptimizedNodeManager service) {
        super(id, service);
    }

    public long getId() {
        return id;
    }

    public void delete() {
        this.dhtService.deleteNode(new Long(this.getId()));
    }

    public Iterable<Relationship> getRelationships() {
        return (Iterable<Relationship>) this.relationships;
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
        Collection<Relationship> c = new ArrayList<Relationship>();
        if (this.relationships.size() > 0) {
            if (dir == Direction.BOTH) {
                return this.getRelationships();
            } else {
                for (Relationship rel : relationships) {
                    if ((((OptimizedRelationship) rel).getStartNodeId() == this.getId())
                            && dir == Direction.OUTGOING) {
                        c.add(rel);
                    } else if ((((OptimizedRelationship) rel).getEndNodeId() == this.getId()) && dir == Direction.INCOMING) {
                        c.add(rel);
                    }
                }
            }
        }
        return c;
    }

    public boolean hasRelationship(Direction dir) {
        if (this.relationships.size() > 0) {
            if (dir == Direction.BOTH) {
                return true;
            } else {
                for (Relationship rel : relationships) {
                    if ((((OptimizedRelationship) rel).getStartNodeId() == this.getId())
                            && dir == Direction.OUTGOING) {
                        return true;
                    } else if ((((OptimizedRelationship) rel).getEndNodeId() == this.getId())
                            && dir == Direction.INCOMING) {
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
            if (dir == Direction.BOTH) {
                return this.getRelationships(type);
            } else {
                for (Relationship rel : relationships) {
                    if (rel.getType() == type) {
                        if (((OptimizedRelationship) rel).getStartNodeId() == this.getId()
                                && dir == Direction.OUTGOING) {
                            c.add(rel);
                        } else if (((OptimizedRelationship) rel).getEndNodeId() == this.getId()
                                && dir == Direction.INCOMING) {
                            c.add(rel);
                        }
                    }
                }
            }
        }
        return c;
    }

    public boolean hasRelationship(RelationshipType type, Direction dir) {
        if (this.relationships.size() > 0) {
            if (dir == Direction.BOTH) {
                return this.hasRelationship(type);
            } else {
                for (Relationship rel : relationships) {
                    if (rel.getType() == type) {
                        if (((OptimizedRelationship) rel).getStartNodeId() == this.getId()
                                && dir == Direction.OUTGOING) {
                            return true;
                        } else if (((OptimizedRelationship) rel).getEndNodeId() == this.getId()
                                && dir == Direction.INCOMING) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Relationship getSingleRelationship(RelationshipType type, Direction dir) {
        if (this.relationships.size() > 0) {
            if (dir == Direction.BOTH) {
                for (Relationship rel : this.relationships) {
                    if (rel.getType() == type) {
                        return rel;
                    }
                }
            } else {
                for (Relationship rel : relationships) {
                    if (rel.getType() == type) {
                        if (((OptimizedRelationship) rel).getStartNodeId() == this.getId()
                                && dir == Direction.OUTGOING) {
                            return rel;
                        } else if (((OptimizedRelationship) rel).getEndNodeId() == this.getId()
                                && dir == Direction.INCOMING) {
                            return rel;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Relationship createRelationshipTo(Node otherNode, RelationshipType type) {
        //TODO this can be optimized by taking of the getRelationships(...) and doing everything in the foreach
        Iterable<Relationship> relIt = this.getRelationships(type, Direction.OUTGOING);
        //check if there are previous relationships
        for (Relationship rel : relationships) {
            if (((OptimizedRelationship) rel).getEndNodeId() == otherNode.getId()
                    && rel.getType() == type) {
                return rel; //returns an existing relationship instead of creating a new one
            }
        }
        //else create relationship
        Relationship rel = null;
        try {
            rel = this.dhtService.createRelationship(this.id, otherNode.getId(), type);
        } catch (NullPointerException e) {
            System.out.println(this.dhtService); //@NULL This is the null
            e.printStackTrace();
        }
        this.addRelationship(rel);
        return rel;
    }

    protected Relationship addRelationship(Relationship rel) {
        this.relationships.add(rel);
        return rel;
    }

    public Traverser traverse(Order traversalOrder,
            StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator,
            RelationshipType relationshipType, Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Null direction");
        }
        if (relationshipType == null) {
            throw new IllegalArgumentException("Null relationship type");
        }
        // rest of parameters will be validated in traverser package
        return this.traverserFactory.createTraverser(traversalOrder, this, relationshipType, direction, stopEvaluator, returnableEvaluator);
    }

    public Traverser traverse(Order traversalOrder,
            StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator,
            RelationshipType firstRelationshipType, Direction firstDirection,
            RelationshipType secondRelationshipType, Direction secondDirection) {
        if (firstDirection == null || secondDirection == null) {
            throw new IllegalArgumentException("Null direction, "
                    + "firstDirection=" + firstDirection + "secondDirection="
                    + secondDirection);
        }
        if (firstRelationshipType == null || secondRelationshipType == null) {
            throw new IllegalArgumentException("Null rel type, " + "first="
                    + firstRelationshipType + "second=" + secondRelationshipType);
        }
        // rest of parameters will be validated in traverser package
        RelationshipType[] types = new RelationshipType[2];
        Direction[] dirs = new Direction[2];
        types[0] = firstRelationshipType;
        types[1] = secondRelationshipType;
        dirs[0] = firstDirection;
        dirs[1] = secondDirection;
        return this.traverserFactory.createTraverser(traversalOrder, this, types, dirs, stopEvaluator, returnableEvaluator);
    }

    public Traverser traverse(Order traversalOrder,
            StopEvaluator stopEvaluator, ReturnableEvaluator returnableEvaluator,
            Object... relationshipTypesAndDirections) {
        int length = relationshipTypesAndDirections.length;
        if ((length % 2) != 0 || length == 0) {
            throw new IllegalArgumentException("Variable argument should "
                    + " consist of [RelationshipType,Direction] pairs");
        }
        int elements = relationshipTypesAndDirections.length / 2;
        RelationshipType[] types = new RelationshipType[elements];
        Direction[] dirs = new Direction[elements];
        int j = 0;
        for (int i = 0; i < elements; i++) {
            Object relType = relationshipTypesAndDirections[j++];
            if (!(relType instanceof RelationshipType)) {
                throw new IllegalArgumentException(
                        "Expected RelationshipType at var args pos " + (j - 1)
                        + ", found " + relType);
            }
            types[i] = (RelationshipType) relType;
            Object direction = relationshipTypesAndDirections[j++];
            if (!(direction instanceof Direction)) {
                throw new IllegalArgumentException(
                        "Expected Direction at var args pos " + (j - 1)
                        + ", found " + direction);
            }
            dirs[i] = (Direction) direction;
        }
        return this.traverserFactory.createTraverser(traversalOrder, this, types, dirs, stopEvaluator, returnableEvaluator);
    }

    protected Relationship deleteRelationship(long aLong) {
        for (int i = 0; i < relationships.size(); i++) {
            if (this.relationships.get(i).getId() == aLong) {
                relDirection.remove(i);
                Relationship rel = this.relationships.remove(i);
                OptimizedNode node = (OptimizedNode) rel.getEndNode();
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
        final OptimizedNode other = (OptimizedNode) obj;
        if (this.getId() == other.getId()) {
            return true;
        } else {
            return false;
        }
    }
}
