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
public abstract class Message implements Serializable {

    public class Request extends Message {
    }

    public class Response extends Message {
    }

    public class PutMessage extends Message {
    }

    public class GetMessage extends Message {
    }
}

