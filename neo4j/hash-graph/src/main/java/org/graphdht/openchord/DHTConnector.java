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

/**
 *
 * @author nuno
 */
public class DHTConnector<K extends Serializable, V extends Serializable> implements DHTService<K, V> {

    public static void main(String[] args) throws RemoteException {
        DHTConnector dc = new DHTConnector("127.0.0.1", DHTConstants.GDHT_OPENCHORD_I_PORT);
        System.out.println("conn = " + dc.connect());
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
    private DHTService stub;
    private final String name;

    public DHTConnector(String host, int port) {
        this.host = host;
        this.name = DHTConstants.GDHT_RMI_BASENAME + port;
        System.out.println(name);
    }

    public boolean connect() {
        try {
            stub = (DHTService) RMIManager.findRemoteObject(host, name);
            System.out.println("stub = " + stub);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void release() {
        stub = null;
    }

    @Override
    public V get(K key) throws RemoteException {
        return (V) stub.get(key);
    }

    @Override
    public V put(K key, V value) throws RemoteException {
        return (V) stub.put(key, value);
    }

    @Override
    public V remove(K key) throws RemoteException {
        return (V) stub.remove(key);
    }

    @Override
    public void putAll(Map<K, V> m) throws RemoteException {
        stub.putAll(m);
    }
}
