package org.graphdht.dht;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Hello world!
 *
 */
public class DHTDeamon {

    private static DHTService service;

    public static void main(String[] args) {
        System.out.println("Setting GraphDHT node UP!");
        guilistening();
        consolelistening();
        service = new DHTServer();
    }

    private static void shutdown() {
        System.out.println("exitting...");
//        service.shutdown();
    }

    private static void guilistening() {
        JFrame j = new JFrame("Chord Node Control");
        JButton jb = new JButton("Shutdown");
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                shutdown();
            }
        });
        j.add(jb);
        j.setLocation(200, 200);
        j.pack();
        j.setVisible(true);
    }

    private static void consolelistening() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Scanner s = new Scanner(System.in);
                while (true) {
                    System.out.println("Enter command...");
                    String line = s.nextLine();
                    if (line.equalsIgnoreCase("exit")) {
                        shutdown();
                        return;
                    }
                }
            }
        }).start();
    }
}
