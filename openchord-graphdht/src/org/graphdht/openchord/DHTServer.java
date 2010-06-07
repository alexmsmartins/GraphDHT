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

import de.uniba.wiai.lspi.chord.service.ServiceException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import static org.graphdht.openchord.DHTConstants.*;

/**
 * 
 *
 *
 *
 * @param <K>
 * @param <V>
 * @author nmsa@dei.uc.pt
 */
public class DHTServer<K extends Serializable, V extends Serializable> extends UnicastRemoteObject implements DHTService<K, V> {

    private final ChordWrapper chord;
    private final String name;

    public DHTServer(ChordWrapper chord) throws RemoteException {
        super();
        this.chord = chord;
        this.name = GDHT_RMI_BASENAME + (chord.getURL().getPort() > 0 ? chord.getURL().getPort() : GDHT_OPENCHORD_I_PORT);
        System.out.println(name);
    }

    public void start() {
        boolean binded = RMIManager.bindRemoteObject(name, this);
        if (binded) {
            System.out.println("RMI : GraphDHT node UP!");
        } else {
            System.out.println("RMI: Failure setting node UP!");
        }
    }

    @Override
    public V get(K key) throws RemoteException {
        return (V) chord.get(new DHTKey(key)); // HERE
    }

    @Override
    public V put(K key, V value) throws RemoteException {
        return (V) chord.put(new DHTKey(key), value);
    }

    @Override
    public V remove(K key) throws RemoteException {
        return (V) chord.remove(new DHTKey(key));
    }

    @Override
    public void putAll(Map<K, V> values) throws RemoteException {
        Map<DHTKey, Serializable> map = new HashMap<DHTKey, Serializable>();
        for (K key : values.keySet()) {
            map.put(new DHTKey(key), values.get(key));
        }
        chord.putAll(map);
    }
}
