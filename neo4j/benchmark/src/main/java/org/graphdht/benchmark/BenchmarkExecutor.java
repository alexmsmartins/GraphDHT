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
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import org.graphdht.hashgraph.Constants;
import org.graphdht.hashgraph.OptimizedHashGraphDatabase;
import org.graphdht.hashgraph.SimpleHashGraphDatabase;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 *
 *
 * @author nmsa@dei.uc.pt
 * @author pamm@dei.uc.pt
 */
public class BenchmarkExecutor {

    private static final Runtime runtime = Runtime.getRuntime();
    private static final String KILL_CHORD = "bash -c \"kill -9 `ps ax | grep openchord | awk '{print \\$1}'`\"";
    private static String cmdInit = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Init localhost 5000";
    private static String cmdJoin = "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.Join localhost 5000 localhost ";
    private static final String unixDeleteVar = "rm -drf var ";
    private static final String windowsDeleteVar = "cmd /c rmdir /S /Q var";
    private static String deleteVar = unixDeleteVar;
    /**
     * minimum 2 nodes...
     */
    private static final int NUMBER_OF_NODES = 5;
//    private static final ProcessManager[] procs = new ProcessManager[NUMBER_OF_NODES];
    private static FileOutputStream mainLogFile;
    private static FileOutputStream timesFile;
    private static FileOutputStream chordLogFile;
    private static boolean embebed = false;
    private static boolean simple = false;
    private static boolean openchord = false;
    private static boolean optimized_openchord = true;
    private static boolean optimized = true;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Adapt classpath separator if needed...
        if (System.getProperty("os.name").contains("Windows")) {
            cmdInit = cmdInit.replaceAll(":", ";");
            cmdJoin = cmdJoin.replaceAll(":", ";");
            deleteVar = windowsDeleteVar;
        }
        // Start logging system...
        try {
            new File("log/").mkdir();
            timesFile = new FileOutputStream("log/test-times.log");
            mainLogFile = new FileOutputStream("log/test-main.log");
            chordLogFile = new FileOutputStream("log/test-chord.log");
        } catch (Exception e) {
            mainLog("Cannot init log files...");
            System.exit(0);
        }
        // Start testing phase
        mainLog("Please, kill all openchord active processes before start testing...\n\n");
        GraphDatabaseService service;
        long duration;
        File workload = new File("workload/");
        mainLog("Working on: " + workload.getAbsolutePath());
        for (File testfile : workload.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".dot");
            }
        })) {
            mainLog("Testing file: " + testfile);
            if (embebed) {
                //--------------------EmbeddedGraphDatabase--------------------//
                mainLog("EmbeddedGraphDatabase\t" + testfile.getName());
                try {
                    Process p = runtime.exec(deleteVar);
                    System.out.println(p.waitFor());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                service = new EmbeddedGraphDatabase("var/test");
                Transaction tx = service.beginTx();
                duration = readFileIntoNeo(testfile, service);
                tx.finish();
                mainLog("Duration: " + duration);
                timesLog(testfile.getName() + "\tEmbeddedGraphDatabase\t" + duration);
                service.shutdown();
            }
            //
            //
            //
            if (simple) {
                //--------------------SimpleHashGraphDatabase-simple--------------------//
                // Not necessary to clean
                mainLog("SimpleHashGraphDatabase-simple\t" + testfile.getName());
                service = new SimpleHashGraphDatabase("simple");
                duration = readFileIntoNeo(testfile, service);
                mainLog("Duration: " + duration);
                timesLog(testfile.getName() + "\tSimpleHashGraphDatabase-simple\t" + duration);
                service.shutdown();
            }
            //
            //
            //
            if (optimized) {
                //--------------------SimpleHashGraphDatabase-simple--------------------//
                // Not necessary to clean
                mainLog("OptimizedHashGraphDatabase-simple\t" + testfile.getName());
                service = new OptimizedHashGraphDatabase("simple");
                duration = readFileIntoNeo(testfile, service);
                mainLog("Duration: " + duration);
                timesLog(testfile.getName() + "\tOptimizedHashGraphDatabase-optimized\t" + duration);
                service.shutdown();
            }
            //
            //
            //
            if (openchord) {
                //--------------------SimpleHashGraphDatabase-openchord--------------------//
                mainLog("Please start new chord... and press enter...");
                scanner.nextLine();
//                // Initialize the chord processes
//                procs[0] = new ProcessManager(cmdInit, 5000);
//                for (int i = 1; i < NUMBER_OF_NODES; i++) {
//                    procs[i] = new ProcessManager(cmdJoin + (5000 + i), 5000 + i);
//                }
//                mainLog("Waiting for chord to be ready...");
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//                mainLog("Chord ready...");

                mainLog("SimpleHashGraphDatabase-openchord\t" + testfile.getName());
                service = new SimpleHashGraphDatabase("openchord");
                duration = readFileIntoNeo(testfile, service);
                mainLog("Duration: " + duration);
                timesLog(testfile.getName() + "\tSimpleHashGraphDatabase-openchord\t" + duration);
//                mainLog("Cleaning chord...");
//                // Kill chord processes
//                for (ProcessManager process : procs) {
//                    process.kill();
//                }
//                for (ProcessManager process : procs) {
//                    process.waitfinish();
//                }
//                mainLog("// Clean");
//                // Shutdown Neo4J
                service.shutdown();
            }
            //
            //
            //
            if (optimized_openchord) {
                //--------------------OptimizedHashGraphDatabase-openchord--------------------//
                mainLog("Please start new chord... and press enter...");
                scanner.nextLine();
//                // Initialize the chord processes
//                procs[0] = new ProcessManager(cmdInit, 5000);
//                for (int i = 1; i < NUMBER_OF_NODES; i++) {
//                    procs[i] = new ProcessManager(cmdJoin + (5000 + i), 5000 + i);
//                }
//                mainLog("Waiting for chord to be ready...");
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//                mainLog("Chord ready...");

                mainLog("OptimizedHashGraphDatabase-openchord\t" + testfile.getName());
                service = new OptimizedHashGraphDatabase("openchord");
                duration = readFileIntoNeo(testfile, service);
                mainLog("Duration: " + duration);
                timesLog(testfile.getName() + "\tOptimizedHashGraphDatabase-openchord\t" + duration);
//                mainLog("Cleaning chord...");
//                // Kill chord processes
//                for (ProcessManager process : procs) {
//                    process.kill();
//                }
//                for (ProcessManager process : procs) {
//                    process.waitfinish();
//                }
//                mainLog("// Clean");
//                // Shutdown Neo4J
                service.shutdown();
            }

        }
    }

    /**
     * Executes the test and returns the duration of the process
     *
     * @param testfile
     * @param service
     * @return
     */
    private static long readFileIntoNeo(File testfile, GraphDatabaseService service) {
        long inititalTime = System.currentTimeMillis();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(testfile));
        } catch (FileNotFoundException fnf) {
            return -1;
        }

        String line = null;
        Node node = null, previousNode;
        HashMap<Long, Long> addedNodes = new HashMap<Long, Long>();
        try {
            //read and ignore first line
            reader.readLine();
        } catch (IOException ex) {
        }
        //read each line of the
        long lines = 0;
        while (true) {
            try {
                line = reader.readLine().trim();
                lines++;
            } catch (IOException io) {
            }
            if (line.charAt(0) == '}') {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
                return System.currentTimeMillis() - inititalTime;
            }
            if (line.charAt(line.length() - 1) == ';') {
                line = line.substring(0, line.length() - 1);
            }
            String[] tokens = line.split("--");
            previousNode = null;
            for (String str : tokens) {
                Long id = Long.parseLong(str.trim());
                if (addedNodes.containsKey(id)) {
                    node = service.getNodeById(addedNodes.get(id));
                } else {
                    node = service.createNode();
                    node.setProperty("id", id.toString());
                    addedNodes.put(id, node.getId());
                }
                //add relationship between this node and the previous one
                if (previousNode != null) {
                    previousNode.createRelationshipTo(node, Constants.MyRelationshipType.KNOWS);
                }
                previousNode = node;
            }
            if (lines % 10000 == 1000) {
                System.out.println(new Date() + " lines: " + lines);
            }
        }
    }

    /**
     * System.out.println (""); can be commented...
     *
     */
    public static void timesLog(String data) {
        try {
            timesFile.write((data + "\n").getBytes());
            timesFile.flush();
            System.out.println(data);
        } catch (IOException ex) {
        }
    }

    /**
     * System.out.println (""); can be commented...
     *
     */
    public static void mainLog(String data) {
        try {
            mainLogFile.write((data + "\n").getBytes());
            mainLogFile.flush();
            System.out.println(data);
        } catch (IOException ex) {
        }
    }

    /**
     * System.out.println (""); can be commented...
     *
     */
    public static void chordLog(String data) {
        try {
            chordLogFile.write(data.getBytes());
//            chordLogFile.flush();
//            System.out.println(data);
        } catch (IOException ex) {
        }
    }

    public static class ProcessManager extends Thread {

        private final String prefix;
        private InputStream is;
        private Process process;
        private boolean run = true;

        public ProcessManager(String comand, int code) {
            this.prefix = "[" + Integer.toString(code) + "] ";
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
