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
 *
 * @author nuno
 */
public class Message implements Serializable {

    public enum MessageType implements Serializable {

        GET, PUT, REMOVE, PUTALL, GETALL
    }
    public MessageType type;
    public Object obj;

    public Message(MessageType type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return type + " > " + obj;
    }
}
