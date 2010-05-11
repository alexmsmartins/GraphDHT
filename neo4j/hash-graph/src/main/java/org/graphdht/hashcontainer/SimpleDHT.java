/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.graphdht.hashcontainer;

import java.rmi.RemoteException;
import java.util.Map;
import org.graphdht.dht.rmi.DHTService;

/**
 *
 * @author alex
 */
public class SimpleDHT<T> implements DHTService<Long, T> {

    public T get(Long key) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public T put(Long key, T value) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public T remove(Long key) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void putAll(Map<Long,T> m) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
