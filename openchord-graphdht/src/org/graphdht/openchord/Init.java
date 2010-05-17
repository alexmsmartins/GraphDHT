package org.graphdht.openchord;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 *
 * @author nuno
 */
public class Init {

    public static void main(String[] args) {
        String ip = "localhost";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException ex) {
        }
        String port = "5000";
        if (args.length > 0) {
            if (args[0].contains("help")) {
                System.out.println("java org.graphdht.openchord.Init [ip]:[port]");
                System.exit(0);
            } else {
                String[] split = args[0].trim().split(":");
                if (split[0] != null && split[0].length() > 0) {
                    ip = split[0];
                }
                if (split[1] != null && split[1].length() > 0) {
                    port = split[1];
                }
            }
        }
        de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
        String url = protocol + "://" + ip + ":" + port + "/";
        URL localURL = null;
        try {
            localURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
        try {
            chord.create(localURL);
            System.out.println("Chord Started at " + localURL);
        } catch (ServiceException e) {
            throw new RuntimeException("Could not create DHT!", e);
        }
    }
}
