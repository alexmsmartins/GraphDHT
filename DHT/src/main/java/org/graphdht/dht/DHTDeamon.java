package org.graphdht.dht;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Hello world!
 *
 */
public class DHTDeamon {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static Registry registry;
    private static String DHT_OBJECT_NAME = "DHTServiceObject";

    public static void main(String[] args) {
        System.out.println("Setting GraphDHT node UP!");
        DHTService stub = null;
        try {
            stub = new DHTServer();
            registry = LocateRegistry.createRegistry(5001);
            registry.bind(DHT_OBJECT_NAME, stub);
            System.out.println("\n\nGraphDHT node UP!\n\n");
        } catch (Exception e) {
            System.out.println("Cannot set up GraphDHT node!!!");
            e.printStackTrace();
            System.exit(0);
        }

        addToNetwork();
        listening(stub.toString());
    }

    private static void addToNetwork() {
        System.out.println("added... or some...");
    }

    private static void shutdown() {
        System.out.println("removed... or some...");
        try {
            registry.unbind(DHT_OBJECT_NAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    private static void listening(String name) {
        JFrame j = new JFrame(name);

        JButton jb = new JButton("Shutdown");
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("GUIexitting...");
                shutdown();
            }
        });
        j.add(jb);
        j.pack();
        j.setVisible(true);
//        j.setLocation(null);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    System.out.println("Enter command...");
                    String line = readLn(20);
                    if (line.equalsIgnoreCase("exit")) {
                        System.out.println("exitting...");
                        shutdown();
                        return;
                    }

                }
            }
        }).start();
    }

    private static String readLn(int maxLg) { //utility function to read from stdin
        byte lin[] = new byte[maxLg];
        int lg = 0, car = -1;
        try {
            while (lg < maxLg) {
                car = System.in.read();
                if ((car < 0) || (car == '\n')) {
                    break;
                }
                lin[lg++] += car;
            }
        } catch (IOException e) {
            return (null);
        }
        if ((car < 0) && (lg == 0)) {
            return (null);  // eof
        }
        return new String(lin, 0, lg).trim();
    }
}
