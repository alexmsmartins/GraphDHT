/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphdht.hashcontainer;

import java.util.HashMap;
import java.util.Map;

import org.graphdht.dht.HTService;

/**
 *
 * @author alex
 */
public class SimpleHT<K, T> implements HTService<K, T> {

    HashMap chordDHTInFuture;

    public SimpleHT() {
        super();
        chordDHTInFuture = new HashMap<K, T>();
    }

    public T get(K aLong) {
        return (T) this.chordDHTInFuture.get(aLong);
    }

    public void put(K aLong, T t) {
        this.chordDHTInFuture.put(aLong, t);
    }

    public void remove(K aLong) {
        this.remove(aLong);
    }

    public void putAll(Map<K, T> longTMap) {
        this.chordDHTInFuture.putAll(longTMap);
    }

    public Iterable<T> getAllValues() {
        return chordDHTInFuture.values();
    }

    @Override
    public void shutdown() {
        return;
    }

    public Iterable<K> getAllKeys() {
        return chordDHTInFuture.keySet();
    }
}
