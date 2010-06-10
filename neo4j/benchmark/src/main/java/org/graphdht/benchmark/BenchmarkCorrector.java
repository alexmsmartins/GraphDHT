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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Filters incorrect lines
 * adds ; at the end
 *
 *
 * @author nmsa@dei.uc.pt
 */
public class BenchmarkCorrector {

    private static int BUFFER_SIZE = 1024 * 1024;

    public static void main(String[] args) {
        File workload = new File("workload/");
        for (File testfile : workload.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".dot");
            }
        })) {
            String file = testfile.toString();
            System.out.println("File: " + testfile);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException fnf) {
                fnf.printStackTrace();
            }
            String line = null;
//            FileOutputStream fo = null;
//            StringBuilder buffer = null;
//            try {
//                buffer = new StringBuilder(reader.readLine());
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            try {
//                fo = new FileOutputStream(file.replaceAll(".dot", ".new.dot"));
//            } catch (IOException io) {
//                io.printStackTrace();
//            }
            //read each line of the
            boolean running = true;
            boolean lineOK;
            int lines = 0;
            try {
                while (running) {
                    line = reader.readLine().trim();
                    if (line.equals("}")) {
                        running = false;
                        System.out.println(lines);
                    } else {
                        lines++;
//                        lineOK = true;
//                        if (line.charAt(line.length() - 1) == ';') {
//                            line = line.substring(0, line.length() - 1);
//                        }
//                        String[] tokens = line.split("--");
//                        HashSet<String> set = new HashSet<String>();
//                        for (String str : tokens) {
//                            if (!set.add(str.trim())) {
//                                lineOK = false;
//                            }
//                        }
//                        if (lineOK) {
//                            buffer.append("\n\t");
//                            buffer.append(line);
//                            buffer.append(";");
//                            if (buffer.length() > BUFFER_SIZE) {
//                                fo.write(buffer.toString().getBytes());
//                                buffer = new StringBuilder(BUFFER_SIZE);
//                            }
//                        }
                    }
                }
//                buffer.append("\n}");
//                fo.write(buffer.toString().getBytes());
//                fo.close();

            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
}
