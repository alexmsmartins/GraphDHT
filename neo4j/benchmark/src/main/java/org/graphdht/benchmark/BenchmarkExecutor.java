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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.graphdht.hashgraph.Constants;
import org.graphdht.hashgraph.SimpleHashGraphDatabase;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author nuno
 */
public class BenchmarkExecutor {

    private static final Runtime runtime = Runtime.getRuntime();
    public static final String KILL_CHORD = "bash -c \"kill -9 `ps ax | grep openchord | awk '{print \\$1}'`\"";
    public static final String CMD_INIT = "java -cp graphdht-oc.jar;config;lib/openchord_1.0.5.jar;lib/log4j.jar org.graphdht.openchord.Init localhost 5000";
    public static final String CMD_JOIN = "java -cp graphdht-oc.jar;config;lib/openchord_1.0.5.jar;lib/log4j.jar org.graphdht.openchord.Join localhost 5000 localhost ";
    /**
     * At least 2...
     */
    public static final int NUMBER_OF_NODES = 5;
    public static final ProcessManager[] procs = new ProcessManager[NUMBER_OF_NODES];
    private static FileOutputStream mainLogFile;
    private static FileOutputStream timesFile;
    private static FileOutputStream chordLogFile;

    public static void main(String[] args) {
        try {
            // @NUNO Files to store and separate the output
            // Not necessary to use something like log4j...
            timesFile = new FileOutputStream("workload/test-times.log");
            mainLogFile = new FileOutputStream("workload/test-main.log");
            chordLogFile = new FileOutputStream("workload/test-chord.log");
        } catch (FileNotFoundException ex) {
            mainLog("Cannot init log file...");
            System.exit(0);
        }
        mainLog("Please, kill all openchord processes using : \n" + KILL_CHORD);
        GraphDatabaseService service;
        long duration;
        File workload = new File("workload/");
        for (File testfile : workload.listFiles()) {
            if (testfile.getName().matches("\\S+(dot)")) {
                mainLog(testfile.getName());
                // @NUNO
                // EmbeddedGraphDatabase does not work...


//                //--------------------EmbeddedGraphDatabase--------------------//
//                mainLog(testfile + "\tEmbeddedGraphDatabase\t");
//                System.out.println("Clean old data...");
//                new File("var").delete();
//                System.out.println("cleaned...");
//                service = new EmbeddedGraphDatabase("var/test");
//                duration = readFileIntoNeo(testfile, service);
//                mainLog("Duration: " + duration);
//                timesLog(testfile.getName() + "\tEmbeddedGraphDatabase\t" + duration);
//                service.shutdown();
//                //--------------------SimpleHashGraphDatabase-simple--------------------//
                // Not necessary to clean
//                mainLog(testfile + "\tSimpleHashGraphDatabase-simple\t");
//                service = new SimpleHashGraphDatabase("simple");
//                duration = readFileIntoNeo(testfile, service);
//                mainLog("Duration: " + duration);
//                timesLog(testfile.getName() + "\tSimpleHashGraphDatabase-simple\t" + duration);
//                service.shutdown();

                //--------------------SimpleHashGraphDatabase-openchord--------------------//
                mainLog("Start new chord...");
                procs[0] = new ProcessManager(CMD_INIT, 5000);
                for (int i = 1; i < NUMBER_OF_NODES; i++) {
                    procs[i] = new ProcessManager(CMD_JOIN + (5000 + i), 5000 + i);
                }
                mainLog("Waiting for chord to be ready...");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                mainLog("Chord ready...");

                mainLog(testfile + "\tSimpleHashGraphDatabase-openchord\t");

                service = new SimpleHashGraphDatabase("openchord");
                duration = readFileIntoNeo(testfile, service);

                mainLog("Duration: " + duration);
                timesLog(testfile.getName() + "\tSimpleHashGraphDatabase-openchord\t" + duration);
                mainLog("Cleaning chord...");
                for (ProcessManager process : procs) {
                    process.kill();
                }
                for (ProcessManager process : procs) {
                    process.waitfinish();
                }
                mainLog("// Clean");
                // Shutdown Neo4J
                service.shutdown();
            }
        }
    }

    /**
     * Executes the test and returns the duration of the process
     *
     * @param testfile
     * @param serv
     * @return 
     */
    private static long readFileIntoNeo(File testfile, GraphDatabaseService serv) {
        long inititalTime = System.currentTimeMillis();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(testfile));
        } catch (FileNotFoundException fnf) {
            return -1;
        }

        String line;
        Node node = null, previousNode;
        HashMap<Long, Long> addedNodes = new HashMap<Long, Long>();
        try {
            //read and ignore first line
            reader.readLine();
        } catch (IOException ex) {
        }
        //read each line of the
        while (true) {
            try {
                line = reader.readLine().trim();
                if (line.charAt(0) == '}') {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                    }
                    return System.currentTimeMillis() - inititalTime;
                }
                line = line.substring(0, line.length() - 1); //excludes the ';' character
                String[] tokens = line.trim().split(" -- ");
                previousNode = null;
                for (String str : tokens) {
                    str = str.trim();
                    Long id = Long.parseLong(str);
                    if (addedNodes.get(id) != null) {
                        node = serv.getNodeById(id);
                    } else {
                        node = serv.createNode();
                        node.setProperty("id", id.toString());
                        addedNodes.put(id, id);
                    }
                    //add relationship between this node and the previous one
                    if (previousNode != null) {
                        previousNode.createRelationshipTo(node, Constants.MyRelationshipType.KNOWS);
                        previousNode = node;
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    public static void timesLog(String data) {
        try {
            timesFile.write((data + "\n").getBytes());
            System.out.println(data);
        } catch (IOException ex) {
        }
    }

    public static void mainLog(String data) {
        try {
            mainLogFile.write((data + "\n").getBytes());
            System.out.println(data);
        } catch (IOException ex) {
        }
    }

    public static void chordLog(String data) {
        try {
            chordLogFile.write((data + "\n").getBytes());
            System.out.println(data);
        } catch (IOException ex) {
        }
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
                    chordLog(prefix + readLn);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        private void waitfinish() {
            try {
                this.interrupt();
                final int exitValue = process.waitFor();
                mainLog("Exit Value" + exitValue);
            } catch (Exception ex) {
            }
        }

        private void kill() {
            this.run = false;
            this.process.destroy();
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
