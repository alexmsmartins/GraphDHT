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

import java.io.File;
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
import java.util.concurrent.TimeUnit;
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
    private Logger logger;
    private long comunication;
    private long working;

    public DHTServer(DHTChord chord) {
        super();
        this.chord = chord;
        this.port = GDHT_OPENCHORD_SERVER_ADD + (chord.getURL().getPort() > 0 ? chord.getURL().getPort() : GDHT_OPENCHORD_I_PORT);
        logger = new Logger();
    }

    public void start() throws IOException {
        ServerSocket s = new ServerSocket(port);
        System.out.println("DHTServer listening at: " + port);
        while (true) {
            new ListeningThread(s.accept());
        }
    }

    public class Logger extends Thread {

        private FileOutputStream logFile;
        private boolean running = true;
        private StringBuffer buffer;
        private int p;
        private int r;
        private int g;
        private int ga;
        private int pa;
        private String last = "Moelix :D";

        private Logger() {
            running = true;
            buffer = new StringBuffer("\nStarted!\n");
            try {
                new File("../log/").mkdir();
                logFile = new FileOutputStream("../log/DHTServer-" + port + "-debug.log", true);
            } catch (Exception ex) {
                System.out.println("Cannot start log");
            }
            this.start();
        }

        public void shutdown() {
            running = false;
        }

        @Override
        public void run() {
            StringBuffer local;
            while (running) {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException ie) {
                }
                synchronized (this) {
                    local = buffer;
                    buffer = new StringBuffer(new Date().toString());
                    buffer.append("\n");
                }
                local.append("P:   ").append(p);
                local.append("\nG:   ").append(g);
                local.append("\nR:   ").append(r);
                local.append("\nPA:  ").append(pa);
                local.append("\nGA:  ").append(ga);
                local.append("\nCOM: ").append(comunication);
                local.append("\nWORK:").append(working).append("\n");
                local.append("\nLast:").append(this.last).append("\n");
                try {
                    logFile.write(local.toString().getBytes());
                } catch (IOException io) {
                }
            }
        }

        private void logE(Exception e) {
            buffer.append(e);
            buffer.append("\n");
        }

        private void logR(String last) {
            this.last = "R" + last;
            r++;
        }

        private void logG(String last) {
            this.last = "G" + last;
            g++;
        }

        private void logP(String last) {
            this.last = "P" + last;
            p++;
        }

        private void logPA(String last) {
            this.last = "PA" + last;
            pa++;
        }

        private void logGA() {
            this.last = "GA";
            ga++;
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
            long one, two = 0;
            System.out.println("DHTServer thread is spawning after receiving a socket");
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
                    one = System.currentTimeMillis();
                    message = (Message) is.readObject();
                    two = System.currentTimeMillis();
                    comunication += two - one;
                } catch (IOException ex) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        logger.logE(ex1);
                    }
                    return;
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                if (message == null) {
                    System.out.println(socket.getPort() + " message == null");
                    if (socket.isClosed()) {
                        System.out.println(socket.getPort() + " CLOSED...");
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
                        one = System.currentTimeMillis();
                        working += one - two;
                        os.writeObject(response);
                        two = System.currentTimeMillis();
                        comunication += two - one;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public V get(K key) {
        logger.logG(key.toString());
        try {
            return (V) chord.get(new DHTKey(key)); // HERE
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
            return null;
        } finally {
            //  debug("DONE " + key);
        }
    }

    @Override
    public V put(K key, V value) {
        logger.logP(key.toString() + ":" + value.toString());
        try {
            return (V) chord.put(new DHTKey(key), value);
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
            return null;
        } finally {
            //  debug("DONE " + key);
        }
    }

    @Override
    public V remove(K key) {
        logger.logR(key.toString());
        try {
            return (V) chord.remove(new DHTKey(key));
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
            return null;
        } finally {
            //  debug("DONE " + key);
        }
    }

    @Override
    public void putAll(Map<K, V> values) {
        logger.logPA(values.toString());
        try {
            Map<DHTKey, Serializable> map = new HashMap<DHTKey, Serializable>();
            for (K key : values.keySet()) {
                map.put(new DHTKey(key), values.get(key));
            }
            chord.putAll(map);
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
        } finally {
            //  debug("DONE " + values);
        }
    }

    @Override
    public Iterable<V> getAllValues() {
        logger.logGA();
        return null;
    }
}
