package org.graphdht.hashgraph;

import java.io.Serializable;

/**
 * Contains functionality that is common to both node and relationship. This includes methods
 * to handle Id related functionality
 * User: alex
 * Date: 25/Mai/2010
 * Time: 1:10:44
 * To change this template use File | Settings | File Templates.
 */
public class SimplePrimitive extends SimplePropertyContainer implements Serializable {

    protected long id;
    transient SimpleNodeManager dhtService;

    /**
     * This method is meant to be called by a Node or a Relationship implementation and never directly
     *
     * @param id unique identifier for the Primitive 
     * @param service HashMap like service parametrized to the corresponding subclass
     */
    protected SimplePrimitive(long id, SimpleNodeManager service) {
        super();
        this.id = id;
        this.dhtService = service;
    }
}
