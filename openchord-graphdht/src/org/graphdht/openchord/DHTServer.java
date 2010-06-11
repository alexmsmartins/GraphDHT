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
public class DHTServer implements HTService<Long, byte[]> {

    private final DHTChord chord;
    private final int port;
    private Logger logger;
    private long comTime;
    private long worTime;
    private long comCount = 0;
    private long worCount = 0;

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
        private int g;
        private int other;

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
                local.append("P     :").append(p);
                local.append("\tG   :").append(g);
                local.append("\tOT  :").append(other);
                local.append("\tCOM :").append(comTime).append(" AVG:").append((double) comTime / (double) comCount);
                local.append("\tWORK:").append(worTime).append(" AVG:").append((double) worTime / (double) worCount);
                local.append("\n");
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

        private void logG(String last) {
            g++;
        }

        private void logP(String last) {
            p++;
        }

        private void logO() {
            other++;
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
                    comTime += two - one;
                    comCount++;
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
                    byte[] response = null;
                    switch (message.type) {
                        case GET:
                            response = get(message.key);
                            one = System.currentTimeMillis();
                            worTime += one - two;
                            worCount++;
                            try {
                                os.writeObject(response);
                            } catch (IOException io) {
                                io.printStackTrace();
                            }
                            two = System.currentTimeMillis();
                            comTime += two - one;
                            comCount++;
                            break;
                        case PUT:
                            put(message.key, message.byteArray);
                            break;
                        case REMOVE:
                            remove(message.key);
                            break;
                        case PUTALL:
                            putAll(null);
                            break;
                        case GETALL:
                            getAllValues();
                            break;
                    }
                    one = System.currentTimeMillis();
                    worTime += one - two;
                    worCount++;
                }
            }
        }
    }

    @Override
    public byte[] get(Long key) {
        logger.logG(key.toString());
        try {
            return (byte[]) chord.get(new DHTKey(key)); // HERE
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void put(Long key, byte[] value) {
        logger.logP(key.toString());
        try {
            chord.put(new DHTKey(key), value);
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Long key) {
        logger.logO();
        try {
            chord.remove(new DHTKey(key));
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
        }
    }

    @Override
    public void putAll(Map<Long, byte[]> values) {
        logger.logO();
        try {
            Map<DHTKey, Serializable> map = new HashMap<DHTKey, Serializable>();
            for (Long key : values.keySet()) {
                map.put(new DHTKey(key), values.get(key));
            }
            chord.putAll(map);
        } catch (Exception e) {
            logger.logE(e);
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<byte[]> getAllValues() {
        logger.logO();
        return null;
    }

    @Override
    public void shutdown() {
    }
}
