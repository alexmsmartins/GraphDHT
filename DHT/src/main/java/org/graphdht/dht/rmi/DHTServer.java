/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphdht.dht.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import org.graphdht.dht.obj.Key;
import org.graphdht.dht.obj.Value;

/**
 *
 * @param <Key>
 * @param <Value>
 * @author root
 */
public class DHTServer extends UnicastRemoteObject implements DHTService {

    public DHTServer() throws RemoteException {
        super();
    }

    @Override
    public Value get(Key key) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public Value put(Key key, Value value) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public Value remove(Key key) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public void putAll(Map<Key, Value> m) {
        System.out.println("To implement!!!");
    }
}
