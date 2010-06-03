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
package org.graphdht.openchord.rmi;

import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.Key;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
public class DHTServer<K extends Key, V extends Serializable> extends UnicastRemoteObject implements DHTService<K, V> {

    private final Chord chord;
    private final String name;

    public DHTServer(Chord chord) throws RemoteException {
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
        System.out.println("TO BE COMPLETED");
        try {
            return (V) chord.retrieve(key);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public V put(K key, V value) throws RemoteException {
        System.out.println("TO BE COMPLETED");
        try {
            chord.insert(key, value);
            System.out.println("insert");
            return value;
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public V remove(K key) throws RemoteException {
        System.out.println("TO BE IMPLEMENTED");
//        try {
//            chord.remove(key, key);
//            System.out.println("insert");
//            return value;
//        } catch (ServiceException ex) {
//            ex.printStackTrace();
            return null;
//        }
    }

    @Override
    public void putAll(Map<K, V> m) throws RemoteException {
        System.out.println("TO BE IMPLEMENTED...");
    }
}
