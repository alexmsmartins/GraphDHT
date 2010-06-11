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

import org.graphdht.dht.HTService;
import org.graphdht.hashcontainer.HTServiceFactory;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/Jun/2010
 * Time: 10:12:22
 * To change this template use File | Settings | File Templates.
 */
public class OpenChordHTServiceFactory<V extends Serializable> implements HTServiceFactory {

    @Override
    public HTService createHTService() {
        DHTConnector service = new DHTConnector();
        service.connect();
        return service;
    }
}
