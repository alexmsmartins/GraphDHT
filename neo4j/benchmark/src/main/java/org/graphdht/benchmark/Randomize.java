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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author nuno
 */
public class Randomize {

    private static final int BUFFER_SIZE = 256 * 1024;
    private static final double MAX_PROBABILITY = 0.3;
    private static final double MIN_PROBABILITY = 0.000000000001;
    private int connected[];
    private double popular[];
    private final String graphname;
    private final Random random;
    private final String filename;
    private final int nodeCount;

    /**
     * 
     * @param graphname
     * @param random
     * @param filename
     */
    public Randomize(String graphname, Random random, String filename, int nodeCount) {
        this.graphname = graphname;
        this.random = random;
        this.filename = filename;
        this.nodeCount = nodeCount;
    }

    public void generate() {
        try {
            connected = new int[nodeCount];
            popular = new double[nodeCount];
            StringBuilder buffer = new StringBuilder(BUFFER_SIZE).append("graph " + graphname + " {\n");
            FileOutputStream fos = new FileOutputStream(filename);
            popular[0] = random.nextGaussian();
            for (int i = 1; i < nodeCount; i++) {
                double gauss = random.nextGaussian();
                popular[i] = Math.pow(2, gauss * 5);
                for (int j = 0; j < i; j++) {
                    final double rando = random.nextDouble();
                    if (rando < probability(i, popular[i], popular[j], connected[j], i - j)) {
                        connected[i]++;
                        connected[j]++;
                        buffer.append(" ").append(j).append(" -- ").append(i).append("\n");
                    }
                }
                if (buffer.length() > BUFFER_SIZE) {
                    fos.write(buffer.toString().getBytes());
                    buffer = new StringBuilder(BUFFER_SIZE);
                }
            }
            buffer.append("}");
            fos.write(buffer.toString().getBytes());
            fos.close();

            double sum = 0;
            int max = 0, min = Integer.MAX_VALUE;
            for (int i : connected) {
                if (i > max) {
                    max = i;
                }
                if (i < min) {
                    min = i;
                }
                sum += i;
            }
            StringBuilder sb = new StringBuilder(graphname);
            System.out.println("Randomize size: " + nodeCount);
            System.out.println("AVG: " + (sum / nodeCount));
            System.out.println("min = " + min);
            System.out.println("max = " + max);
            System.out.println("-----------------------");

            sb.append("Randomize size:\t " + nodeCount);
            sb.append("AVG: \t" + (sum / nodeCount));
            sb.append("min: \t" + min);
            sb.append("max: \t" + max);
            Arrays.sort(connected);
            sb.append("\n");
            for (int i : connected) {
                sb.append(i).append("\n");
            }
            FileOutputStream fosDist = new FileOutputStream(filename + ".xls");
            fosDist.write(sb.toString().getBytes());
            fosDist.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    ///

    private double probability(double world, double popular0, double popular1, double connected, double distance) {
        double probability;
        probability = connected + 1;
        probability *= popular0 + popular1;
        probability /= world * 200;
        probability = Math.pow(probability, 5);
        probability /= distance / nodeCount;
        if (probability > MAX_PROBABILITY) {
            probability = MAX_PROBABILITY;
        } else if (probability < MIN_PROBABILITY) {
            probability = MIN_PROBABILITY;
        }
        return probability;
    }
}
