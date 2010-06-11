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

import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.chord.com.Entry;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.AsynChord;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.Report;
import de.uniba.wiai.lspi.chord.com.CommunicationException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.graphdht.dht.HTService;

/**
 * Implements all operations which can be invoked on the local node.
 *
 *
 * @see Chord
 * @see  Report
 * @see  AsynChord
 *
 *
 * @author Karsten Loesing
 * @version 1.0.5
 */
public class DHTChord extends ChordImpl implements HTService<DHTKey, Serializable> {

    public DHTChord() {
        super();
    }

    @Override
    public Serializable get(DHTKey key) {
        // check parameters
        if (key == null) {
            NullPointerException e = new NullPointerException("Key must not have value null!");
            this.logger.error("Null pointer", e);
            throw e;
        }
        // determine ID for key
        ID id = this.hashFunction.getHashKey(key);
        Set<Entry> result = null;
        boolean retrieved = false;
        while (!retrieved) {
            // find successor of id
            Node responsibleNode = null;
            responsibleNode = findSuccessor(id);
            // invoke retrieveEntry method
            try {
                result = responsibleNode.retrieveEntries(id);
                // cause while loop to end.
                retrieved = true;
            } catch (CommunicationException e1) {
                e1.printStackTrace();
                continue;
            }
        }
        for (Entry entry : result) {
            return entry.getValue();
        }
        return null;
    }

    @Override
    public void put(DHTKey key, Serializable value) {
        // check parameters
        if (key == null || value == null) {
            throw new NullPointerException(
                    "Neither parameter may have value null!");
        }
        // determine ID for key
        ID id = this.hashFunction.getHashKey(key);
        Entry entryToInsert = new Entry(id, value);

        boolean inserted = false;
        while (!inserted) {
            // find successor of id
            Node responsibleNode;
            // try {
            responsibleNode = this.findSuccessor(id);
            // invoke insertEntry method
            try {
                Set<Entry> entries = responsibleNode.retrieveEntries(id);
                while (!entries.isEmpty()) {
                    Entry t = entries.iterator().next();
                    if (t != null) {
                        entries.remove(t);
                    }
                }
                responsibleNode.insertEntry(entryToInsert);
                inserted = true;
            } catch (CommunicationException e1) {
                e1.printStackTrace();
                continue;
            }
        }
    }

    @Override
    public void remove(DHTKey key) {
        // check parameters
        if (key == null) {
            throw new NullPointerException("The parameter cannot have value null!");
        }
        // determine ID for key
        ID id = this.hashFunction.getHashKey(key);
        boolean inserted = false;
        while (!inserted) {
            // find successor of id
            Node responsibleNode;
            // try {
            responsibleNode = this.findSuccessor(id);
            // invoke insertEntry method
            try {
                Set<Entry> entries = responsibleNode.retrieveEntries(id);
                while (!entries.isEmpty()) {
                    Entry t = entries.iterator().next();
                    if (t != null) {
                        entries.remove(t);
                    }
                }
            } catch (CommunicationException e1) {
                continue;
            }
        }
    }

    @Override
    public void putAll(Map<DHTKey, Serializable> m) {
        for (DHTKey key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public Iterable<Serializable> getAllValues() {
        return null;
    }

    @Override
    public void shutdown() {
    }
}

