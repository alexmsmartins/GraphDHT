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

/**
 * Very simple message to be send through the network
 *
 * @author nmsa@dei.uc.pt
 */
public class Message implements Serializable {

    public short type;
    public long key;
    public byte[] byteArray;

    public Message(short type, long key, byte[] byteArray) {
        this.type = type;
        this.key = key;
        this.byteArray = byteArray;
    }
}
