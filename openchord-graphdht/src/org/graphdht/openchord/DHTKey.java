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

import de.uniba.wiai.lspi.chord.service.Key;
import java.io.Serializable;

/**
 *
 * @author nuno
 */
public class DHTKey implements Key, Serializable {

    private final Serializable key;

    public DHTKey(Serializable key) {
        this.key = key;
    }

    @Override
    public byte[] getBytes() {
        //TODO: Future improvements...
        return key.toString().getBytes();
    }
}
