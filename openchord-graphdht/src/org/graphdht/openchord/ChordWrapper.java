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

import static de.uniba.wiai.lspi.util.logging.Logger.LogLevel.DEBUG;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import de.uniba.wiai.lspi.chord.com.CommunicationException;
import de.uniba.wiai.lspi.chord.com.Entry;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Key;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

/**
 * Implements all operations which can be invoked on the local node.
 *
 * @author Karsten Loesing
 * @version 1.0.5
 */
public class ChordWrapper extends ChordImpl {

    /**
     * Creates a new instance of ChordImpl which initially is disconnected.
     * Constructor is hidden. Only constructor.
     */
    public ChordWrapper() {
        super();
    }

    public void insert(Key key, Serializable s) {
        // check parameters
        if (key == null || s == null) {
            throw new NullPointerException(
                    "Neither parameter may have value null!");
        }
        // determine ID for key
        ID id = this.hashFunction.getHashKey(key);
        Entry entryToInsert = new Entry(id, s);

        boolean debug = this.logger.isEnabledFor(DEBUG);
        if (debug) {
            this.logger.debug("Inserting new entry with id " + id);
        }
        boolean inserted = false;
        while (!inserted) {
            // find successor of id
            Node responsibleNode;
            // try {
            responsibleNode = this.findSuccessor(id);

            if (debug) {
                this.logger.debug("Invoking insertEntry method on node "
                        + responsibleNode.getNodeID());
            }

            // invoke insertEntry method
            try {
                responsibleNode.insertEntry(entryToInsert);
                inserted = true;
            } catch (CommunicationException e1) {
                if (debug) {
                    this.logger.debug(
                            "An error occured while invoking the insertEntry method "
                            + " on the appropriate node! Insert operation "
                            + "failed!", e1);
                }
                continue;
            }
        }
        this.logger.debug("New entry was inserted!");
    }

    public Set<Serializable> retrieve(Key key) {

        // check parameters
        if (key == null) {
            NullPointerException e = new NullPointerException(
                    "Key must not have value null!");
            this.logger.error("Null pointer", e);
            throw e;
        }

        // determine ID for key
        ID id = this.hashFunction.getHashKey(key);

        boolean debug = this.logger.isEnabledFor(DEBUG);
        if (debug) {
            this.logger.debug("Retrieving entries with id " + id);
        }
        Set<Entry> result = null;

        boolean retrieved = false;
        while (!retrieved) {
            // find successor of id
            Node responsibleNode = null;

            responsibleNode = this.findSuccessor(id);

            // invoke retrieveEntry method
            try {
                result = responsibleNode.retrieveEntries(id);
                // cause while loop to end.

                retrieved = true;
            } catch (CommunicationException e1) {
                if (debug) {
                    this.logger.debug(
                            "An error occured while invoking the retrieveEntry method "
                            + " on the appropriate node! Retrieve operation "
                            + "failed!", e1);
                }
                continue;
            }
        }
        Set<Serializable> values = new HashSet<Serializable>();

        if (result != null) {
            for (Entry entry : result) {
                values.add(entry.getValue());
            }
        }

        this.logger.debug("Entries were retrieved!");

        return values;

    }

    public void remove(Key key, Serializable s) {

        // check parameters
        if (key == null || s == null) {
            throw new NullPointerException(
                    "Neither parameter may have value null!");
        }

        // determine ID for key
        ID id = this.hashFunction.getHashKey(key);
        Entry entryToRemove = new Entry(id, s);

        boolean removed = false;
        while (!removed) {

            boolean debug = this.logger.isEnabledFor(DEBUG);
            if (debug) {
                this.logger.debug("Removing entry with id " + id
                        + " and value " + s);
            }

            // find successor of id
            Node responsibleNode;
            responsibleNode = findSuccessor(id);

            if (debug) {
                this.logger.debug("Invoking removeEntry method on node "
                        + responsibleNode.getNodeID());
            }
            // invoke removeEntry method
            try {
                responsibleNode.removeEntry(entryToRemove);
                removed = true;
            } catch (CommunicationException e1) {
                if (debug) {
                    this.logger.debug(
                            "An error occured while invoking the removeEntry method "
                            + " on the appropriate node! Remove operation "
                            + "failed!", e1);
                }
                continue;
            }
        }
        this.logger.debug("Entry was removed!");
    }
}
