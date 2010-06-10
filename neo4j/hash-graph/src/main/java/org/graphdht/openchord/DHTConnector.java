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
import java.net.Socket;
import java.util.Map;
import org.graphdht.dht.HTService;
import org.graphdht.openchord.Message.MessageType;

/**
 * Now this is the only copy of the file...
 *
 *
 * @author nuno
 */
public class DHTConnector<K extends Serializable, V extends Serializable> implements HTService<K, V>, Serializable {

    public static long hitCounter = 0;

    public static void main(String[] args) {
        DHTConnector dc = new DHTConnector("127.0.0.1", DHTConstants.GDHT_OPENCHORD_SERVER_ADD + DHTConstants.GDHT_OPENCHORD_I_PORT);
        dc.connect();
        String key = "10000";
        Serializable put = dc.put(key, "cenass1");
        System.out.println("put = " + put);
        put = dc.put(key, "cenass2");
        System.out.println("put = " + put);
        put = dc.put(key, "cenass3");
        System.out.println("put = " + put);
        Serializable get = dc.get(key);
        System.out.println("get = " + get);
    }
    /**
     *
     *
     *
     */
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    public DHTConnector() {
        this("10.3.3.68", DHTConstants.GDHT_OPENCHORD_SERVER_ADD + DHTConstants.GDHT_OPENCHORD_I_PORT);
    }

    public DHTConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            this.socket = new Socket(host, port);
            this.os = new ObjectOutputStream(socket.getOutputStream());
            this.is = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to Open Chord at " + host + ":" + port);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void release() {
        try {
            this.socket.close();
            this.socket = null;
            this.is = null;
            this.os = null;
        } catch (IOException ex) {
            System.out.println("Error releasing: " + ex);
        }
    }

    public Object xchange(Message m) {
        while (true) {
            try {
                os.writeObject(m);
                Object read = is.readObject();
                return read;
            } catch (Exception ex) {
                System.out.println("Reconnecting...");
                //TODO: Better exception handling code...
                release();
                connect();
            }
        }

    }

    @Override
    public V get(K key) {
        hitCounter++;
        return (V) xchange(new Message(MessageType.GET, key));
    }

    @Override
    public V put(K key, V value) {
        hitCounter++;
        return (V) xchange(new Message(MessageType.PUT, new Object[]{key, value}));
    }

    @Override
    public V remove(K key) {
        hitCounter++;
        return (V) xchange(new Message(MessageType.REMOVE, key));
    }

    @Override
    public void putAll(Map<K, V> m) {
        hitCounter++;
        xchange(new Message(MessageType.PUTALL, m));
    }

    @Override
    public Iterable<V> getAllValues() {
        hitCounter++;
        return (Iterable<V>) xchange(new Message(MessageType.GETALL, null));
    }

    @Override
    public void shutdown() {
        this.release();
    }
}
