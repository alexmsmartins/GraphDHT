/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashgraph;

import java.util.Hashtable;
import java.util.Map;
import org.neo4j.graphdb.PropertyContainer;

/**
 *
 * @author alex
 */
public class SimplePropertyContainer implements PropertyContainer {
    Map<String, Object>  properties = new Hashtable();

    public boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public Object getProperty(String key, Object defaultValue) {
        return this.properties.containsKey(key)?this.properties.get(key):defaultValue;
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public Object removeProperty(String key) {
       return this.properties.remove(key);
    }

    public Iterable<String> getPropertyKeys() {
        return (Iterable<String>) this.properties.keySet().iterator();
    }

    public Iterable<Object> getPropertyValues() {
        return (Iterable<Object>) this.properties.values().iterator();
    }

}
