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

/**
 *
 * @author nuno
 */
public class DHTConstants {

    public static final int GDHT_OPENCHORD_I_PORT = 5000;
    public static final int GDHT_OPENCHORD_SERVER_ADD = 1000;
    public static final int GDHT_OCHORD_MAXATTEMPTS = 5;
    public static final String GOC_JOIN_HELP_MESSAGE = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Join bootStrapIP bootStrapPort localIP localPort ";
    public static final String GOC_INIT_HELP_MESSAGE = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Init localIP localPort ";
    public static final String GDHT_LOCALHOST = "127.0.0.1";
    public static final int GDHT_SIMULATION_NODECOUNT = 2;
    public static final String GDHT_RMI_BASENAME = "GDHTd_";

    private DHTConstants() {
    }
}
