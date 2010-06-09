package org.graphdht.benchmark;

import org.graphdht.hashgraph.Constants;
import org.graphdht.hashgraph.SimpleHashGraphDatabase;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/Jun/2010
 * Time: 20:39:50
 * To change this template use File | Settings | File Templates.
 */
public class DOT2Neo4J {

    public static void main(String[] args) {
        long startTime, endTime, varTime;
        BufferedReader reader ;
        GraphDatabaseService service;
        DOT2Neo4J dot2neo = new DOT2Neo4J();
        File workload = new File("../benchmark/workload/");
        for (File testfile : workload.listFiles()) {
            if (testfile.getName().matches("\\S+(dot)")) {
                System.out.println(testfile);

                

                try {
                    //--------------------SimpleHashGraphDatabase-simple--------------------//
                    System.out.print(testfile + "\tSimpleHashGraphDatabase-simple\t");
                    reader = new BufferedReader(new FileReader(testfile));
                    service = new SimpleHashGraphDatabase("simple");

                    startTime = Calendar.getInstance().getTimeInMillis();
                    dot2neo.readFileIntoNeo(reader, service);

                    endTime = Calendar.getInstance().getTimeInMillis();
                    varTime = Calendar.getInstance().getTimeInMillis();
                    System.out.println(varTime);

                    service.shutdown();
                    //--------------------SimpleHashGraphDatabase-openchord--------------------//
                    System.out.print(testfile + "\tSimpleHashGraphDatabase-openchord\t");
                    reader = new BufferedReader(new FileReader(testfile));

                    Process proc = Runtime.getRuntime().exec("sh graphdht-oc.sh");
                    
/*                    BufferedInputStream bin = new BufferedInputStream(proc.getInputStream());

                    for(int k = bin.read(); k != 0; k=bin.read() )
                        System.out.print( (char)k);*/

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    service =                       new SimpleHashGraphDatabase("openchord");

                    startTime = Calendar.getInstance().getTimeInMillis();
                    dot2neo.readFileIntoNeo(reader, service);

                    endTime = Calendar.getInstance().getTimeInMillis();
                    varTime = Calendar.getInstance().getTimeInMillis();
                    System.out.println(varTime);
                    Runtime.getRuntime().exec("killall xterm");
                    service.shutdown();
                    //--------------------EmbeddedGraphDatabase--------------------//
                    System.out.print(testfile + "\tEmbeddedGraphDatabase\t");
                    reader = new BufferedReader(new FileReader(testfile));


                    startTime = Calendar.getInstance().getTimeInMillis();
                    dot2neo.readFileIntoNeo(reader, new EmbeddedGraphDatabase("var/test"));

                    endTime = Calendar.getInstance().getTimeInMillis();
                    varTime = Calendar.getInstance().getTimeInMillis();
                    System.out.println(varTime);

                    service.shutdown();
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
            if (line.charAt(0) == '}')
                break;
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
}
