/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashcontainer;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.graphdht.dht.obj.Key;
import org.graphdht.dht.obj.Value;
import org.graphdht.dht.rmi.DHTService;

/**
 *
 * @author alex
 */
public class SimpleDHT<T> implements DHTService<Long, T> {

    HashMap chordDHTInFuture;

    public SimpleDHT(){
        super();
        chordDHTInFuture = new HashMap<Long,T>();
    }

    public T get(Long aLong) throws RemoteException {
        return (T) this.chordDHTInFuture.get(aLong);
    }

    public T put(Long aLong, T t) throws RemoteException {
        return (T) this.chordDHTInFuture.put(aLong,t);
    }

    public T remove(Long aLong) throws RemoteException {
        return this.remove(aLong);
    }

    public void putAll(Map<Long, T> longTMap) throws RemoteException {
        this.chordDHTInFuture.putAll(longTMap);
    }

    public Iterable<T> getAllValues(){
        return chordDHTInFuture.values();
    }

    public Iterable<Long> getAllKeys(){
        return chordDHTInFuture.keySet();
    }

}
