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
package org.graphdht.openchord.api;

import de.uniba.wiai.lspi.chord.service.Key;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import org.graphdht.openchord.DHTConstants;
import org.graphdht.openchord.LongKey;
import org.graphdht.openchord.rmi.DHTService;

/**
 *
 * @author nuno
 */
public class DHTConnector<K extends Key, V extends Serializable> implements DHTService<K, V> {

    public static void main(String[] args) {
        System.out.println("Testing...");
        DHTConnector dc = new DHTConnector("127.0.0.1", DHTConstants.GDHT_OPENCHORD_I_PORT);
        dc.connect();
        final LongKey key = new LongKey();
        dc.put(key, "cenass");
        Serializable get = dc.get(key);
        System.out.println(":::::" + get + "::");


    }
    private final String host;
    private final int port;
    private DHTService stub;
    private final String name;

    public DHTConnector(String host, int port) {
        this.host = host;
        this.port = port;
        this.name = "rmi://" + host + ":4099/" + DHTConstants.GDHT_RMI_BASENAME + port;
        System.out.println(name);

    }

    public boolean connect() {
        try {
            //        reference =
            Remote lookup = Naming.lookup(name);
            System.out.println("r: " + lookup);
            System.out.println("r: " + lookup.toString());
            System.out.println("r: " + lookup.getClass());
            System.out.println("r: " + lookup.hashCode());
            return true;
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public V get(K key) {
        return (V) stub.get(key);
    }

    @Override
    public V put(K key, V value) {
        return (V) stub.put(key, value);
    }

    @Override
    public V remove(K key) {
        return (V) stub.remove(key);
    }

    @Override
    public void putAll(Map<K, V> m) {
        stub.putAll(m);
    }
}
