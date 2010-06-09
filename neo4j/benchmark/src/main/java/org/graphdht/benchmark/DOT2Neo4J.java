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
package org.graphdht.benchmark;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import org.graphdht.hashgraph.Constants;
import org.graphdht.hashgraph.SimpleHashGraphDatabase;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author nuno
 */
public class DOT2Neo4J {

    private static final Runtime runtime = Runtime.getRuntime();
    public static final String KILL_CHORD = "bash -c \"kill -9 `ps ax | grep openchord | awk '{print \\$1}'`\"";
    public static final String CMD_INIT = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Init localhost 5000";
    public static final String CMD_JOIN = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Join localhost 5000 localhost ";
    /**
     * At least 2...
     */
    public static final int NUMBER_OF_NODES = 5;
    public static final ProcessManager[] procs = new ProcessManager[NUMBER_OF_NODES];

    public static void main(String[] args) {
        System.out.println("Please, kill all openchord processes using : \n" + KILL_CHORD);
        long startTime, endTime, varTime;
        BufferedReader reader;
        GraphDatabaseService service;
        DOT2Neo4J dot2neo = new DOT2Neo4J();
        File workload = new File("../benchmark/workload/");
        for (File testfile : workload.listFiles()) {
            if (testfile.getName().matches("\\S+(dot)")) {
                System.out.println(testfile);
                try {
                    //--------------------SimpleHashGraphDatabase-simple--------------------//
//                    System.out.print(testfile + "\tSimpleHashGraphDatabase-simple\t");
//                    reader = new BufferedReader(new FileReader(testfile));
//                    service = new SimpleHashGraphDatabase("simple");
//
//                    startTime = Calendar.getInstance().getTimeInMillis();
//                    dot2neo.readFileIntoNeo(reader, service);
//
//                    endTime = Calendar.getInstance().getTimeInMillis();
//                    varTime = Calendar.getInstance().getTimeInMillis();
//                    System.out.println(varTime);
//
//                    service.shutdown();
                    //--------------------SimpleHashGraphDatabase-openchord--------------------//
                    System.out.println("Start new chord...");
                    procs[0] = new ProcessManager(CMD_INIT, 5000);
                    for (int i = 1; i < NUMBER_OF_NODES; i++) {
                        procs[i] = new ProcessManager(CMD_JOIN + (5000 + i), 5000 + i);
                    }
                    System.out.println("Waiting for chord to be ready...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    System.out.println("Chord ready...");


                    System.out.print(testfile + "\tSimpleHashGraphDatabase-openchord\t");
                    reader = new BufferedReader(new FileReader(testfile));


                    service = new SimpleHashGraphDatabase("openchord");

                    startTime = Calendar.getInstance().getTimeInMillis();
                    dot2neo.readFileIntoNeo(reader, service);

                    endTime = Calendar.getInstance().getTimeInMillis();
                    varTime = Calendar.getInstance().getTimeInMillis();
                    System.out.println(varTime);
                    System.out.println("CLEANING...");
                    for (ProcessManager process : procs) {
                        process.kill();
                    }
                    for (ProcessManager process : procs) {
                        process.waitfinish();

                    }
                    System.out.println("// Clean");
                    // Shutdown Neo4J
                    service.shutdown();
                    //--------------------EmbeddedGraphDatabase--------------------//
//                    System.out.print(testfile + "\tEmbeddedGraphDatabase\t");
//                    reader = new BufferedReader(new FileReader(testfile));
//
//
//                    startTime = Calendar.getInstance().getTimeInMillis();
//                    dot2neo.readFileIntoNeo(reader, new EmbeddedGraphDatabase("var/test"));
//
//                    endTime = Calendar.getInstance().getTimeInMillis();
//                    varTime = Calendar.getInstance().getTimeInMillis();
//                    System.out.println(varTime);
//                    service.shutdown();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readFileIntoNeo(BufferedReader reader, GraphDatabaseService service) throws IOException {
        GraphDatabaseService neo = service;

        String line;
        Node node = null, previousNode;
        HashMap<Long, Long> addedNodes = new HashMap();

        //read first line
        reader.readLine();
        //read each line of the
        while (true) {
            line = reader.readLine();
            if (line.charAt(0) == '}') {
                break;
            }
            line = line.substring(0, line.length() - 1); //excludes the ';' character
            String[] tokens = line.trim().split(" -- ");
            previousNode = null;
            for (String str : tokens) {
                str = str.trim();
                Long id = Long.parseLong(str);
                if (addedNodes.get(id) != null) {
                    node = neo.getNodeById(id);
                } else {
                    node = neo.createNode();
                    node.setProperty("id", id.toString());
                    addedNodes.put(id, id);
                }
                //add relationship between this node and the previous one
                if (previousNode != null) {
                    previousNode.createRelationshipTo(node, Constants.MyRelationshipType.KNOWS);
                    previousNode = node;
                }
            }
        }
        return;
    }

    public static class ProcessManager extends Thread {

        private final String prefix;
        private InputStream is;
        private Process process;
        private boolean run = true;

        public ProcessManager(String comand, int port) {
            this.prefix = "[" + Integer.toString(port) + "] ";
            try {
                this.process = runtime.exec(comand, null, new File("bin/"));
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            this.is = process.getInputStream();
            this.start();
        }

        @Override
        public void run() {
            while (run) {
                String readLn = readLn();
                if (readLn != null) {
                    System.out.println(prefix + readLn);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void waitfinish() {
            try {
                final int exitValue = process.waitFor();
                System.out.println("Exit Value" + exitValue);
            } catch (Exception ex) {
            }
        }

        private void kill() {
            this.run = false;
            this.process.destroy();
            this.interrupt();
        }

        private String readLn() { //utility function to read from stdin
            byte lin[] = new byte[1000];
            int lg = 0, car = -1;
            try {
                while (lg < 1000) {
                    car = is.read();
                    if ((car < 0) || (car == '\n')) {
                        break;
                    }
                    lin[lg++] += car;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return (null);
            }
            if ((car < 0) && (lg == 0)) {
                return (null);  // eof
            }
            return (new String(lin, 0, lg));
        }
    }
}
