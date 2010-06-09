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
 *
 * @author nuno
 */
public class Init {

    public static void main(String[] args) {
        String localURLstr = null;
        de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
        final String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
        try {
            if (args[0].contains("help")) {
                System.out.println(GOC_INIT_HELP_MESSAGE);
                System.exit(0);
            }
            final int port = Integer.parseInt(args[1].trim());
            localURLstr = protocol + "://" + args[0].trim() + ":" + port + "/";
        } catch (Exception e) {
            System.out.println(GOC_INIT_HELP_MESSAGE);
            System.exit(-1);
        }

        URL localURL = null;
        try {
            localURL = new URL(localURLstr);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Could not create DHT!");
        }

        boolean joint = false;
        int att = 0;
        DHTChord chord = new DHTChord();
        do {
            try {
                chord.create(localURL);
                System.out.println("Chord Started at " + localURL);
                joint = true;
            } catch (ServiceException e) {
                if (chord != null) {
                    chord.leave();
                }
            }

        } while (!joint && att++ < GDHT_OCHORD_MAXATTEMPTS);
        if (!joint) {
            throw new RuntimeException("Could not create DHT!");
        }


        try {
            DHTServer server = new DHTServer(chord);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
