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
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 * Starts secondary nodes joining the existing network...
 *
 *
 * @author nmsa@dei.uc.pt
 */
public class Join {

    /**
     *
     *
     *
     * 
     * @param args
     */
    public static void main(String[] args) {
        String ip = "localhost";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException ex) {
        }
        int port = 5001;
        if (args.length > 0) {
            if (args[0].contains("help")) {
                System.out.println("java CreateChord [ip]:[port]");
                System.exit(0);
            } else {
                String[] split = args[0].trim().split(":");
                if (split[0] != null && split[0].length() > 0) {
                    ip = split[0];
                }
                if (split[1] != null && split[1].length() > 0) {
                    port = Integer.parseInt(split[1]);
                }
            }
        }

        de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
        final String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
        String url = protocol + "://" + ip + ":" + port + "/";
        System.out.println("url " + url);
        URL localURL = null, bootstrapURL = null;
        try {
            localURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String bsurl = protocol + "://" + ip + ":" + (port - 1) + "/";
        System.out.println("Bootstrap: " + bsurl);
        try {
            bootstrapURL = new URL(bsurl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
        try {
            chord.join(localURL, bootstrapURL);
            System.out.println("Chord Started at " + localURL);
        } catch (ServiceException e) {
            throw new RuntimeException("Could not create DHT!", e);
        }
    }
}
