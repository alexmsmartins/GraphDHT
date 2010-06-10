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
        int nodeCount = GDHT_SIMULATION_NODECOUNT;
        if (args.length > 0) {
            nodeCount = Integer.parseInt(args[0]);
        }
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
        DHTChord chord = null;
        try {
            chord = new DHTChord();
            chord.create(rootURL);
            System.out.println("Chord Started at " + rootURL);
        } catch (ServiceException e) {
            e.printStackTrace();
            System.exit(0);
        }

        URL nodesURL = null;
        for (int i = 0; i < nodeCount; i++) {
            try {
                nodesURL = new URL(nodeNamePrefix + (nodeId++) + nodeNameSuffix);
            } catch (MalformedURLException ex) {
                throw new RuntimeException("Could not create DHT!", ex);
            }
            try {
                DHTChord cnode = new DHTChord();
                cnode.join(nodesURL, rootURL);
                System.out.println("Chord Started at " + nodesURL);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        try {
            DHTServer server = new DHTServer(chord);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
