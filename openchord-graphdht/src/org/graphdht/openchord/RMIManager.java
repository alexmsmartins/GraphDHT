package org.graphdht.openchord;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static org.graphdht.openchord.DHTConstants.*;

/**
 * This class contains Methods to manage the RMI objects.
 * Starts the rmiregistry when needed and binds or finds remote objects in that registry.
 *
 */
public class RMIManager {

    public static boolean bindRemoteObject(String name, Remote r) {
        for (int i = 0; i < GDHT_RMI_ATTEMPTS; i++) {
            try {
                LocateRegistry.createRegistry(GDHT_RMI_PORT);
            } catch (Exception e) {
            }
            try {
                LocateRegistry.getRegistry(GDHT_RMI_PORT).rebind(name, r);
                return true;
            } catch (Exception e) {
                if (i < GDHT_RMI_ATTEMPTS - 1) {
                    try {
                        Thread.sleep(GDHT_RMI_SLEEP);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
        return false;
    }

    public static Remote findRemoteObject(String host, String name) {
        for (int i = 0; i < GDHT_RMI_ATTEMPTS; i++) {
            try {
                Registry registry = LocateRegistry.getRegistry(host, GDHT_RMI_PORT);
                Remote lookup = registry.lookup(name);
                return lookup;
            } catch (Exception e) {
                if (i < GDHT_RMI_ATTEMPTS - 1) {
                    try {
                        Thread.sleep(GDHT_RMI_SLEEP);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
        return null;
    }

    private RMIManager() {
    }
}
