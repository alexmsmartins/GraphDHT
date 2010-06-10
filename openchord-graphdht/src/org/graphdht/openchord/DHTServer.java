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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
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

    public static final boolean DEBUG = true;
    private FileOutputStream debugLogFile;

    public void debug(String debug) {
        if (DEBUG) {
            if (debugLogFile == null) {
                try {
                    this.debugLogFile = new FileOutputStream("../log/DHTServer-" + port + "-debug.log", true);
                    this.debugLogFile.write(("\n\n" + new Date() + "\n\n").getBytes());
                } catch (Exception ex) {
                }
            }
            try {
                debugLogFile.write((debug + "\n").getBytes());
            } catch (Exception ex) {
            }
        }
    }
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

        private Socket socket;

        private ListeningThread(Socket socket) {
            this.socket = socket;
            this.start();
        }

        @Override
        public void run() {
            debug("DHTServer thread is spawning after receiving a socket");
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
                        debug("cannot close socket: " + ex1);
                    }
                    return;
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                if (message == null) {
                    debug(socket.getPort() + " message == null");
                    debug(socket.getPort() + " in " + socket.isInputShutdown());
                    debug(socket.getPort() + " out " + socket.isOutputShutdown());
                    debug(socket.getPort() + " message == null");
                    if (socket.isClosed()) {
                        debug(socket.getPort() + " CLOSED...");
                    }
                } else {
                    Object response = "null";
                    switch (message.type) {
                        case GET:
                            response = get((K) message.obj);
                            break;
                        case PUT:
                            Object[] objects = (Object[]) message.obj;
                            response = put((K) objects[0], (V) objects[1]);
                            break;
                        case REMOVE:
                            response = remove((K) message.obj);
                            break;
                        case PUTALL:
                            putAll((Map<K, V>) message.obj);
                            break;
                        case GETALL:
                            response = getAllValues();
                            break;
                    }
                    try {
                        debug("Ready to write...");
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
        debug("GET " + key);
        try {
            return (V) chord.get(new DHTKey(key)); // HERE
        } catch (Exception e) {
            debug("Exception " + e);
            e.printStackTrace();
            return null;
        } finally {
            debug("DONE " + key);
        }
    }

    @Override
    public V put(K key, V value) {
        debug("PUT " + key + ":" + value);
        try {
            return (V) chord.put(new DHTKey(key), value);
        } catch (Exception e) {
            debug("Exception " + e);
            e.printStackTrace();
            return null;
        } finally {
            debug("DONE " + key);
        }
    }

    @Override
    public V remove(K key) {
        debug("REMOVE " + key);
        try {
            return (V) chord.remove(new DHTKey(key));
        } catch (Exception e) {
            debug("Exception " + e);
            e.printStackTrace();
            return null;
        } finally {
            debug("DONE " + key);
        }
    }

    @Override
    public void putAll(Map<K, V> values) {
        debug("PUTALL " + values);
        try {
            Map<DHTKey, Serializable> map = new HashMap<DHTKey, Serializable>();
            for (K key : values.keySet()) {
                map.put(new DHTKey(key), values.get(key));
            }
            chord.putAll(map);
        } catch (Exception e) {
            debug("Exception " + e);
            e.printStackTrace();
        } finally {
            debug("DONE " + values);
        }
    }

    @Override
    public Iterable<V> getAllValues() {
        debug("GETALL ");
        return null;
    }
}
