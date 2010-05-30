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

import de.uniba.wiai.lspi.chord.service.Key;
import java.io.Serializable;
import java.util.Set;

/**
 * RMI Interface to be used to preform DHT operations
 * Adapted to correspon to Open Chord Operations.
 *
 * 
 * @author nmsa@dei.uc.pt
 */
public interface OChordService<K extends Key, V extends Serializable> {

    public Set<Serializable> retrieve(Key key);

    public void insert(Key key, Serializable srlzbl);

    public void remove(Key key, Serializable srlzbl);
}
