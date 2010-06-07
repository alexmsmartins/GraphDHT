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

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import static org.graphdht.openchord.DHTConstants.*;

/**
 * Starts secondary nodes joining the existing network...
 *
 *
 * @author nmsa@dei.uc.pt
 */
public class Simulate {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
        final String nodeNamePrefix = "oclocal://node";
        final String nodeNameSuffix = "/";

        int nodeId = 0;

        URL rootURL = null;

        try {
            rootURL = new URL(nodeNamePrefix + (nodeId++) + nodeNameSuffix);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Could not create DHT!", ex);
        }
        ChordWrapper chord = null;
        try {
            chord = new ChordWrapper();
            chord.create(rootURL);
            System.out.println("Chord Started at " + rootURL);
        } catch (ServiceException e) {
            e.printStackTrace();
            System.exit(0);
        }

        URL nodesURL = null;
        for (int i = 0; i < GDHT_SIMULATION_NODECOUNT; i++) {
            try {
                nodesURL = new URL(nodeNamePrefix + (nodeId++) + nodeNameSuffix);
            } catch (MalformedURLException ex) {
                throw new RuntimeException("Could not create DHT!", ex);
            }
            try {
                ChordWrapper cnode = new ChordWrapper();
                cnode.join(nodesURL, rootURL);
                System.out.println("Chord Started at " + nodesURL);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        try {
            DHTServer server = new DHTServer(chord);
            server.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
