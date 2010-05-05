package org.graphdht.dht;

import org.graphdht.dht.rmi.DHTServer;
import org.graphdht.dht.rmi.DHTService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Hello world!
 *
 */
public class DHTDeamon {

    public static void main(String[] args) {
        System.out.println("Setting GraphDHT node UP!");
        try {
            String name = "DHTServiceObject";
            DHTService stub = new DHTServer();
            Registry registry = LocateRegistry.createRegistry(5001);
            registry.rebind(name, stub);
            System.out.println("\n\nGraphDHT node UP!\n\n");
        } catch (Exception e) {
            System.out.println("Cannot set up GraphDHT node!!!");
            e.printStackTrace();
        }
        addToNetwork();
        listenConsole();
    }

    private static void addToNetwork() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void listenConsole() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
