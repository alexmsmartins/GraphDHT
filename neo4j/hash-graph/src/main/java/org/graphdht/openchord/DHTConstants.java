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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author nuno
 */
public class DHTConstants {

    public static final short GET = 1;
    public static final short PUT = 2;
    public static final short REMOVE = 4;
    public static final short PUTALL = 8;
    public static final short GETALL = 16;
    public static final int GDHT_OPENCHORD_I_PORT = 5000;
    public static final int GDHT_OPENCHORD_SERVER_ADD = 1000;
    public static final int GDHT_OCHORD_MAXATTEMPTS = 5;
    public static final String GOC_JOIN_HELP_MESSAGE = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Join bootStrapIP bootStrapPort localIP localPort ";
    public static final String GOC_INIT_HELP_MESSAGE = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Init localIP localPort ";
    public static final String GDHT_LOCALHOST = "127.0.0.1";
    public static final int GDHT_SIMULATION_NODECOUNT = 5;
    public static final String GDHT_RMI_BASENAME = "GDHTd_";

    private DHTConstants() {
    }

    public static byte[] toByteArray(Serializable s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long init = System.currentTimeMillis();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.flush();
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }

    public static Object toObject(byte[] bytes) {
        Object object = null;
        try {
            object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
