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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.graphdht.dht.HTService;
import static org.graphdht.openchord.DHTConstants.*;

/**
 * 
 *
 *
 *
 * @param <K>
 * @param <V>
 * @author nmsa@dei.uc.pt
 */
public class DHTServer<K extends Serializable, V extends Serializable> implements HTService<K, V> {

    private final DHTChord chord;
    private final int port;

    public DHTServer(DHTChord chord) {
        super();
        this.chord = chord;
        this.port = GDHT_OPENCHORD_SERVER_ADD + (chord.getURL().getPort() > 0 ? chord.getURL().getPort() : GDHT_OPENCHORD_I_PORT);
    }

    public void start() throws IOException {
        ServerSocket s = new ServerSocket(port);
        System.out.println("DHTServer listening at: " + port);
        while (true) {
            new ListeningThread(s.accept());
        }
    }

    public class ListeningThread extends Thread {

        private final Socket socket;

        private ListeningThread(Socket socket) {
            this.socket = socket;
            this.start();
        }

        @Override
        public void run() {
            ObjectInputStream is;
            ObjectOutputStream os;
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                is = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }

            while (true) {
                Message message = null;
                try {
                    message = (Message) is.readObject();
                } catch (IOException ex) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        System.out.println("cannot close socket: " + ex1);
                    }
                    return;
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                if (message == null) {
                    System.out.println(socket.getPort() + " message == null");
                    System.out.println(socket.getPort() + " in " + socket.isInputShutdown());
                    System.out.println(socket.getPort() + " out " + socket.isOutputShutdown());
                    System.out.println(socket.getPort() + " message == null");
                    if (socket.isClosed()) {
                        System.out.println(socket.getPort() + " CLOSED...");
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Object response = null;
                    switch (message.type) {
                        case GET:
                            response = get((K) message.obj);
                            ;
                            break;
                        case PUT:
                            Object[] objects = (Object[]) message.obj;
                            response = put((K) objects[0], (V) objects[1]);
                            break;
                        case REMOVE:
                            response = remove((K) message.obj);
                            ;
                            break;
                        case PUTALL:
                            putAll((Map<K, V>) message.obj);
                            break;
                        case GETALL:
                            response = getAllValues();
                            break;

                    }
                    try {
                        os.writeObject(response);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }
    }

    @Override
    public V get(K key) {
        return (V) chord.get(new DHTKey(key)); // HERE
    }

    @Override
    public V put(K key, V value) {
        return (V) chord.put(new DHTKey(key), value);
    }

    @Override
    public V remove(K key) {
        return (V) chord.remove(new DHTKey(key));
    }

    @Override
    public void putAll(Map<K, V> values) {
        Map<DHTKey, Serializable> map = new HashMap<DHTKey, Serializable>();
        for (K key : values.keySet()) {
            map.put(new DHTKey(key), values.get(key));
        }
        chord.putAll(map);
    }

    @Override
    public Iterable<V> getAllValues() {
        return null;
    }
}
