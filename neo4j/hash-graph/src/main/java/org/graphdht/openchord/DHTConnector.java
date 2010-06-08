/**********************************************************
 * Doctoral Program in Science and Information Technology
 * Department of Informatics Engineering
 * University of Coimbra
 **********************************************************
 * Large Scale Concurrent Systems
 *
 * Pedro Alexandre Mesquita Santos Martins - pamm@dei.uc.pt
 * Nuno Manuel dos Santos Antunes - nmsa@dei.uc.pt
 **********************************************************/
package org.graphdht.openchord;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import org.graphdht.dht.HTService;
import org.graphdht.dht.RMIHTService;

/**
 *
 * @author nuno
 */
public class DHTConnector<K extends Serializable, V extends Serializable> implements HTService<K, V> {

    public static void main(String[] args) {
        DHTConnector dc = new DHTConnector("127.0.0.1", DHTConstants.GDHT_OPENCHORD_I_PORT);
        dc.connect();
        String key = "10000";
        Serializable put = dc.put(key, "cenass");
        System.out.println("put = " + put);

        Serializable get = dc.get(key);
        System.out.println("get = " + get);
    }
    /**
     *
     *
     * 
     */
    private final String host;
    private RMIHTService stub;
    private final String name;

    public DHTConnector(String host, int port) {
        this.host = host;
        this.name = DHTConstants.GDHT_RMI_BASENAME + port;
        System.out.println(name);
    }

    public boolean connect() {
        try {
            stub = (RMIHTService) RMIManager.findRemoteObject(host, name);
            System.out.println("stub = " + stub);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void release() {
        stub = null;
    }

    @Override
    public V get(K key) {
        try {
            return (V) stub.get(key);
        } catch (RemoteException rmex) {
            System.out.println("failed to get: " + key);
            rmex.printStackTrace();
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            return (V) stub.put(key, value);
        } catch (RemoteException rmex) {
            System.out.println("failed to put: " + key + " : " + value);
            rmex.printStackTrace();
            return null;
        }
    }

    @Override
    public V remove(K key) {
        try {
            return (V) stub.remove(key);
        } catch (RemoteException rmex) {
            System.out.println("failed to remove: " + key);
            rmex.printStackTrace();
            return null;
        }
    }

    @Override
    public void putAll(Map<K, V> m) {
        try {
            stub.putAll(m);
        } catch (RemoteException rmex) {
            System.out.println("failed to putAll");
            rmex.printStackTrace();
        }
    }

    @Override
    public Iterable<V> getAllValues() {
        try {
            return stub.getAllValues();
        } catch (RemoteException rmex) {
            System.out.println("failed to getAllValues");
            rmex.printStackTrace();
            return null;
        }
    }
}
