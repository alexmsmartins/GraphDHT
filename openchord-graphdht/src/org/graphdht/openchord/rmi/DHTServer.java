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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
 * @author root
 */
public class DHTServer<K extends Key, V extends Serializable> extends UnicastRemoteObject implements DHTService<K, V> {
    

    private final Chord chord;
    private final String name;

    public DHTServer(Chord chord) throws RemoteException {
        super();
        this.chord = chord;
        this.name = GDHT_RMI_BASENAME + chord.getURL().getPort();
    }

    public void start() {
        try {
            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(GDHT_HOST, GDHT_RMIREGISTRY_PORT);
            } catch (Exception e) {
                System.out.println("Get error");
                e.printStackTrace();
            }
            registry.bind(name, this);
            System.out.println("\n\nGraphDHT node UP!\n\n");
        } catch (Exception e) {
            System.out.println("Cannot set up GraphDHT node!!!");
            e.printStackTrace();
        }
    }

    @Override
    public V get(K key) {
        try {
            chord.retrieve(key);
        } catch (ServiceException ex) {
            ex.printStackTrace();
        }
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public V put(K key, V value) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public V remove(K key) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public void putAll(Map<K, V> m) {
        System.out.println("To implement!!!");
    }
}
