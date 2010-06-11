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
import static org.graphdht.openchord.DHTConstants.*;

/**
 * Now this is the only copy of the file...
 *
 *
 * @author nuno
 */
public class DHTConnector implements HTService<Long, Serializable>, Serializable {

    public static void main(String[] args) {
        DHTConnector dc = new DHTConnector("127.0.0.1", DHTConstants.GDHT_OPENCHORD_SERVER_ADD + DHTConstants.GDHT_OPENCHORD_I_PORT);
        dc.connect();
        long key = 10000;
        dc.put(key, "cenass1");
        Serializable get = dc.get(key);
        System.out.println("get = " + get);
        dc.put(key, "cenass2");
        dc.put(key, "cenass3");
        get = dc.get(key);
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
        this("10.3.3.191", DHTConstants.GDHT_OPENCHORD_SERVER_ADD + DHTConstants.GDHT_OPENCHORD_I_PORT);
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
        } catch (Exception ex) {
        }
    }

    public void oneway(Message m) {
        try {
            os.writeObject(m);
        } catch (Exception ex) {
            System.out.println("Reconnecting...");
            //TODO: Better exception handling code...
            release();
            connect();
        }
    }

    @Override
    public Serializable get(Long key) {

        try {
            os.writeObject(new Message(GET, key, null));
            byte[] read = (byte[]) is.readObject();
            return (Serializable) DHTConstants.toObject(read);
        } catch (Exception ex) {
            System.out.println("Reconnecting...");
            //TODO: Better exception handling code...
            release();
            connect();
        }
        return null;
    }

    @Override
    public void put(Long key, Serializable value) {

        oneway(new Message(PUT, key, toByteArray(value)));
    }

    @Override
    public void remove(Long key) {

        oneway(new Message(REMOVE, key, null));
    }

    @Override
    public void putAll(Map<Long, Serializable> m) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<Serializable> getAllValues() {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void shutdown() {
        this.release();
    }
}
