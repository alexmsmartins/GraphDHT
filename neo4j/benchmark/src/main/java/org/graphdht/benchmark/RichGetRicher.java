package org.graphdht.benchmark;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/Jun/2010
 * Time: 16:15:18
 * To change this template use File | Settings | File Templates.
 */
public class RichGetRicher {

    public static void main(String[] args){
        RichGetRicher r = new RichGetRicher();
        r.generateGraphBAModel(50, 50000, 10);
    }

    /**
     *
     * @param initialOrder initial number of nodes from where the algorithm is started
     * @param order final number of nodes that the graph is meant to have at the end
     * @param m number of connections that should be established when a new node is added to the graph 
     */
    public void generateGraphBAModel(int initialOrder, int order, int m ){
        Node randomNode = null, newNode= null;
        List<Node> graphNodes = generateInitialGraph(initialOrder, order);

        for(int i = initialOrder; i < order - 1; i++){
            newNode = new Node(i);
            for(int j=0;j<m;j++){
                randomNode = getRandomNodeFrom(graphNodes);
                graphNodes = addNodeToGraph(graphNodes, newNode);
                graphNodes = connect(graphNodes,
                                     newNode.getId(),
                                     randomNode.getId());
            }
        }
        String graphName = "graph1";
        writeToFile(graphNodes, graphName);
    }

    public void writeToFile(List<Node> graph, String graphName){
        System.out.println("Writing graph of size " + graph.size() + " to file");
              try{
    // Create file
    FileWriter fstream = new FileWriter("graph " + graphName + ".dot");
    BufferedWriter out = new BufferedWriter(fstream);

    out.write("" + graphName + "{\n");
    for(Node n: graph){
        //System.out.println("Writing node with degree " + n.degree + " to file");
        out.write(n.toDotLanguage());
    }
    out.write("}\n");

    //Close the output stream
    out.close();
    }catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
    }

    public List<Node> addNodeToGraph(List<Node> graphNodes, Node node){
        return graphNodes;
    }

    public Node getRandomNodeFrom(List<Node> graph){
        int f = (int)(Math.random()* Node.totalDegree);
        int accumulator = 0;

        for(Node node: graph){
            accumulator += node.degree;
            if(accumulator > f){
                return node; 
            }
        }
        return null;
    }

    /**
     * Generates a Graph where all nodes have at least degree 1.
     * If not like this the node would never join the network 
     */
    public List<Node> generateInitialGraph(int initialOrder, int order){
        if(initialOrder%2 == 0)
            initialOrder = initialOrder >> 1 << 1; //make it an even number
        List<Node> graph = new ArrayList<Node>(order);
        Node n1,n2;
        for(int i=0; i<order; i=i+2){
            n1 = new Node(i);
            n2 = new Node(i+1);
            graph.add(n1);
            graph.add(n2);
            graph= connect(graph, n1.id, n2.id);
        }
        return graph;
    }

    /**
     * Connects two nodes in the graph.
     * Assumes that both nodes are already in the graph.
     * @param nodes
     * @param n1
     * @param n2
     * @return
     */
    public List<Node> connect(List<Node> nodes, long n1, long n2){
        Node node1 = nodes.get( (int) n1);
        Node node2 = nodes.get( (int) n2);
        node1.connectTo(node2);
        node2.connectTo(node1);
        return nodes;
    }



}

class Node{
    public static long totalDegree = 0;
    long id;
    List<Long> relations = new ArrayList<Long>();
    int degree = 0;

    Node(){
    }

    public Node(long id){
        super();
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public void connectTo(Node n){
        assert( this.relations != null);
        assert( this != null);
        this.relations.size();
        for(Long l : this.relations){
           if(l == n.getId()){
               this.degree++;
               totalDegree++;
               return;
           }
        }
        this.degree++;
        totalDegree++;
        this.relations.add(n.id);
    }

    public String toString(){
        return this.toDotLanguage();
    }

    public String toDotLanguage() {
        String str = "  " + this.id;
        for(Long rel : relations){
            str += " -- " + rel.intValue();
        }
        str += ";\n";
        return str;
    }
}
