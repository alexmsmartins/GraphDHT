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

import java.util.Random;

/**
 *
 * @author nuno
 */
public class BenchmarkGenerator {

    public static final String FN_FOLDER = "workload/";
    public static final String FN_SUFIX = ".dot";
    private static final int NUMBER_OF_ATTEMPS = 10;
    private static final int SIZE_MIN = 10000;
    private static final int SIZE_INC = 10000;
    private static final int SIZE_MAX = 100000;

    /**
     *
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int nodeCount = SIZE_MIN; nodeCount <= SIZE_MAX; nodeCount += SIZE_INC) {
            for (int att = 0; att < NUMBER_OF_ATTEMPS; att++) {
                //
                //
                //Sufix
                String code = (nodeCount < SIZE_MAX ? "0" : "") + nodeCount + "-run-" + (att < NUMBER_OF_ATTEMPS ? "0" : "") + att;
                //
                //
                // Alg 1
                Random random = new Random(nodeCount + att);
                String name = "test-n-" + code + "-rich";
                String filename = FN_FOLDER + name + FN_SUFIX;
                RichGetRicher r1 = new RichGetRicher(name.replaceAll("-", ""), random, filename);
                r1.generateGraphBAModel(50, nodeCount, 10);
                //
                //
                // Alg 2
                random = new Random(nodeCount + att);
                name = "test-n-" + code + "-rand";
                filename = FN_FOLDER + name + FN_SUFIX;
                Randomize r2 = new Randomize(name.replaceAll("-", ""), random, filename, nodeCount);
                r2.generate();
            }
        }
    }

    private BenchmarkGenerator() {
    }
}
