///**********************************************************
// * Doctoral Program in Science and Information Technology
// * Department of Informatics Engineering
// * University of Coimbra
// **********************************************************
// * Large Scale Concurrent Systems
// *
// * Pedro Alexandre Mesquita Santos Martins - pamm@dei.uc.pt
// * Nuno Manuel dos Santos Antunes - nmsa@dei.uc.pt
// **********************************************************/
//package org.graphdht.openchord;
//
//import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
//import de.uniba.wiai.lspi.chord.com.Entry;
//import de.uniba.wiai.lspi.chord.com.Node;
//import de.uniba.wiai.lspi.chord.data.ID;
//import de.uniba.wiai.lspi.chord.data.URL;
//import de.uniba.wiai.lspi.chord.service.AsynChord;
//import de.uniba.wiai.lspi.chord.service.Chord;
//import de.uniba.wiai.lspi.chord.service.ChordCallback;
//import de.uniba.wiai.lspi.chord.service.ChordFuture;
//import de.uniba.wiai.lspi.chord.service.ChordRetrievalFuture;
//import de.uniba.wiai.lspi.chord.service.Key;
//import de.uniba.wiai.lspi.chord.service.Report;
//import de.uniba.wiai.lspi.chord.service.ServiceException;
//import de.uniba.wiai.lspi.chord.com.CommunicationException;
//import java.io.Serializable;
//import java.rmi.RemoteException;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Implements all operations which can be invoked on the local node.
// *
// *
// * @see Chord
// * @see  Report
// * @see  AsynChord
// *
// *
// * @author Karsten Loesing
// * @version 1.0.5
// */
//public class ChordWrapper implements DHTService<DHTKey, Serializable>, Chord, AsynChord, Report {
//
//    private final ChordImpl chord;
//
//    public ChordWrapper() {
//        this.chord = new ChordImpl();
//    }
//
//    final Node findSuccessor(ID key) {
//        // check if the local node is the only node in the network
//        Node successor = this.references.getSuccessor();
//        if (successor == null) {
//            if (this.logger.isEnabledFor(INFO)) {
//                this.logger.info("I appear to be the only node in the network, so I am "
//                        + "my own "
//                        + "successor; return reference on me: "
//                        + this.getID());
//            }
//            return this.localNode;
//        } // check if the key to look up lies between this node and its successor
//        else if (key.isInInterval(this.getID(), successor.getNodeID())
//                || key.equals(successor.getNodeID())) {
//
//            // try to reach successor
//            try {
//                // successor.ping(); // if methods returns, successor is alive.
//                // ping removed on 17.09.2007. sven
//                return successor;
//            } catch (Exception e) {
//                // not successful, delete node from successor list and finger
//                // table, and set new successor, if available
//                this.logger.warn("Successor did not respond! Removing it from all "
//                        + "lists and retrying...");
//                this.references.removeReference(successor);
//                return findSuccessor(key);
//            }
//        } // ask closest preceding node found in local references for closest
//        // preceding node concerning the key to look up
//        else {
//            Node closestPrecedingNode = this.references.getClosestPrecedingNode(key);
//            try {
//                if (debug) {
//                    this.logger.debug("Asking closest preceding node known to this node for closest preceding node "
//                            + closestPrecedingNode.getNodeID()
//                            + " concerning key " + key + " to look up");
//                }
//                return closestPrecedingNode.findSuccessor(key);
//            } catch (CommunicationException e) {
//                this.logger.error("Communication failure while requesting successor "
//                        + "for key "
//                        + key
//                        + " from node "
//                        + closestPrecedingNode.toString()
//                        + " - looking up successor for failed node "
//                        + closestPrecedingNode.toString());
//                this.references.removeReference(closestPrecedingNode);
//                return findSuccessor(key);
//            }
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="DHTService<Key, Serializable>">
//    @Override
//    public Serializable get(DHTKey key) throws RemoteException {
//        // check parameters
//        if (key == null) {
//            NullPointerException e = new NullPointerException("Key must not have value null!");
//            throw e;
//        }
//
//        // determine ID for key
//        ID id = this.hashFunction.getHashKey(key);
//
//        Set<Entry> result = null;
//
//        boolean retrieved = false;
//        while (!retrieved) {
//            // find successor of id
//            Node responsibleNode = null;
//
//            responsibleNode = findSuccessor(id);
//
//            // invoke retrieveEntry method
//            try {
//                result = responsibleNode.retrieveEntries(id);
//                // cause while loop to end.
//
//                retrieved = true;
//            } catch (CommunicationException e1) {
//                if (debug) {
//                    this.logger.debug(
//                            "An error occured while invoking the retrieveEntry method "
//                            + " on the appropriate node! Retrieve operation "
//                            + "failed!", e1);
//                }
//                continue;
//            }
//        }
//        Set<Serializable> values = new HashSet<Serializable>();
//
//        if (result != null) {
//            for (Entry entry : result) {
//                values.add(entry.getValue());
//            }
//        }
//
//        this.logger.debug("Entries were retrieved!");
//
//        return values;
//    }
//
//    @Override
//    public Serializable put(DHTKey key, Serializable value) throws RemoteException {
//        // check parameters
//        if (key == null || s == null) {
//            throw new NullPointerException(
//                    "Neither parameter may have value null!");
//        }
//
//        // determine ID for key
//        ID id = this.hashFunction.getHashKey(key);
//        Entry entryToInsert = new Entry(id, s);
//
//        boolean debug = this.logger.isEnabledFor(DEBUG);
//        if (debug) {
//            this.logger.debug("Inserting new entry with id " + id);
//        }
//        boolean inserted = false;
//        while (!inserted) {
//            // find successor of id
//            Node responsibleNode;
//            // try {
//            responsibleNode = this.findSuccessor(id);
//
//            if (debug) {
//                this.logger.debug("Invoking insertEntry method on node "
//                        + responsibleNode.getNodeID());
//            }
//
//            // invoke insertEntry method
//            try {
//                responsibleNode.insertEntry(entryToInsert);
//                inserted = true;
//            } catch (CommunicationException e1) {
//                if (debug) {
//                    this.logger.debug(
//                            "An error occured while invoking the insertEntry method "
//                            + " on the appropriate node! Insert operation "
//                            + "failed!", e1);
//                }
//                continue;
//            }
//        }
//        this.logger.debug("New entry was inserted!");
//    }
//
//    @Override
//    public Serializable remove(DHTKey key) throws RemoteException {
//
//        // check parameters
//        if (key == null || s == null) {
//            throw new NullPointerException(
//                    "Neither parameter may have value null!");
//        }
//
//        // determine ID for key
//        ID id = this.hashFunction.getHashKey(key);
//        Entry entryToRemove = new Entry(id, s);
//
//        boolean removed = false;
//        while (!removed) {
//
//            boolean debug = this.logger.isEnabledFor(DEBUG);
//            if (debug) {
//                this.logger.debug("Removing entry with id " + id
//                        + " and value " + s);
//            }
//
//            // find successor of id
//            Node responsibleNode;
//            responsibleNode = findSuccessor(id);
//
//            if (debug) {
//                this.logger.debug("Invoking removeEntry method on node "
//                        + responsibleNode.getNodeID());
//            }
//            // invoke removeEntry method
//            try {
//                responsibleNode.removeEntry(entryToRemove);
//                removed = true;
//            } catch (CommunicationException e1) {
//                if (debug) {
//                    this.logger.debug(
//                            "An error occured while invoking the removeEntry method "
//                            + " on the appropriate node! Remove operation "
//                            + "failed!", e1);
//                }
//                continue;
//            }
//        }
//        this.logger.debug("Entry was removed!");
//    }
//
//    @Override
//    public void putAll(Map<DHTKey, Serializable> m) throws RemoteException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    // </editor-fold>
//    // <editor-fold defaultstate="collapsed" desc="Chord">
//    @Override
//    public URL getURL() {
//        return chord.getURL();
//    }
//
//    @Override
//    public void setURL(URL nodeURL) throws IllegalStateException {
//        chord.setURL(nodeURL);
//    }
//
//    @Override
//    public ID getID() {
//        return chord.getID();
//    }
//
//    @Override
//    public void setID(ID nodeID) throws IllegalStateException {
//        chord.setID(nodeID);
//    }
//
//    @Override
//    public void create() throws ServiceException {
//        chord.create();
//    }
//
//    @Override
//    public void create(URL localURL) throws ServiceException {
//        chord.create(localURL);
//    }
//
//    @Override
//    public void create(URL localURL, ID localID) throws ServiceException {
//        chord.create(localURL, localID);
//    }
//
//    @Override
//    public void join(URL bootstrapURL) throws ServiceException {
//        chord.join(bootstrapURL);
//    }
//
//    @Override
//    public void join(URL localURL, URL bootstrapURL) throws ServiceException {
//        chord.join(localURL, bootstrapURL);
//    }
//
//    @Override
//    public void join(URL localURL, ID localID, URL bootstrapURL) throws ServiceException {
//        chord.join(localURL, localID, bootstrapURL);
//    }
//
//    @Override
//    public void leave() throws ServiceException {
//        chord.leave();
//
//    }
//
//    @Override
//    public void insert(Key key, Serializable object) throws ServiceException {
//        chord.insert(key, object);
//    }
//
//    @Override
//    public Set<Serializable> retrieve(Key key) throws ServiceException {
//        return chord.retrieve(key);
//    }
//
//    @Override
//    public void remove(Key key, Serializable object) throws ServiceException {
//        chord.remove(key, object);
//    }
//
//    // </editor-fold>
//    // <editor-fold defaultstate="collapsed" desc="Report">
//    @Override
//    public String printEntries() {
//        return chord.printEntries();
//    }
//
//    @Override
//    public String printFingerTable() {
//        return chord.printFingerTable();
//    }
//
//    @Override
//    public String printSuccessorList() {
//        return chord.printSuccessorList();
//    }
//
//    @Override
//    public String printReferences() {
//        return chord.printReferences();
//    }
//
//    @Override
//    public String printPredecessor() {
//        return chord.printPredecessor();
//    }
//    // </editor-fold>
//    // <editor-fold defaultstate="collapsed" desc="AsynChord">
//
//    @Override
//    public void retrieve(Key key, ChordCallback callback) {
//        chord.retrieve(key, callback);
//    }
//
//    @Override
//    public void insert(Key key, Serializable entry, ChordCallback callback) {
//        chord.insert(key, entry, callback);
//    }
//
//    @Override
//    public void remove(Key key, Serializable entry, ChordCallback callback) {
//        chord.remove(key, entry, callback);
//    }
//
//    @Override
//    public ChordRetrievalFuture retrieveAsync(Key key) {
//        return chord.retrieveAsync(key);
//    }
//
//    @Override
//    public ChordFuture insertAsync(Key key, Serializable entry) {
//        return chord.insertAsync(key, entry);
//    }
//
//    @Override
//    public ChordFuture removeAsync(Key key, Serializable entry) {
//        return chord.removeAsync(key, entry);
//    }
//    // </editor-fold>
//}

