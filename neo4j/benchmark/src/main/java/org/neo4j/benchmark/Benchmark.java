/*
 * Copyright (c) 2008 "Neo Technology,"
 *     Network Engine for Objects in Lund AB [http://neotechnology.com]
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.benchmark;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.neo4j.benchmark.reporters.Reporter;
import org.neo4j.benchmark.reporters.TextReporter;
import org.neo4j.benchmark.tests.TestExample;

/**
 * Main class for running the benchmarking framework.
 *
 * 
 * @author Patrik
 */
public class Benchmark {

    static Test[] tests = {new TestExample()};
    static Reporter[] reporters = {new TextReporter()};
    private static final String DEFAULT_CONFIG_FILE = "settings/default";

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("-?")
                    || arg.equals("--help")) {
                printHelpText();
                return;
            }
        }
        Properties settings = loadSettings(args);
        if (settings == null) {
            System.err.println("Aborting");
            return;
        }


        // Any setting X.run will be assumed to be a test identifier.
        List<String> testIdentifiers = new LinkedList<String>();
        for (Object settingName : settings.keySet()) {
            String[] settingNameComponents = ((String) settingName).split("\\.");
            String settingPrefix = settingNameComponents[0];
            if (settingNameComponents.length == 2
                    && "run".equals(settingNameComponents[1])
                    && booleanValue(settings.getProperty((String) settingName))
                    && !testIdentifiers.contains(settingPrefix)) {
                testIdentifiers.add(settingPrefix);
            }
        }
        System.out.print("Will run the following tests: \n --");
        for (String testIdentifier : testIdentifiers) {
            System.out.print(" " + testIdentifier);
        }
        System.out.println();
        double totalScore = 0;
        for (String testIdentifier : testIdentifiers) {
            totalScore += new TestRunner(settings, testIdentifier).Run();
        }
        totalScore /= testIdentifiers.size();
        System.out.println("All tests finished");
        System.out.println("Avg score for all tests: " + ((int) totalScore));
    }

    /**
     * Parses a boolean.
     * @param string
     *            The string to parse.
     * @return True if the input string begins with Y,y,T or t.
     */
    private static boolean booleanValue(String string) {
        return string.length() >= 1
                && "YyTt".contains(string.subSequence(0, 1));
    }

    /**
     * Prints the help message for this program.
     */
    private static void printHelpText() {
        String programName = Benchmark.class.getSimpleName();
        System.out.println("Usage: "
                + programName
                + " [-h][-?][-c CONFIGFILE] [more parameters, semicolon separated]");
        System.out.println("Run a number of chosen benchmarks.");
        System.out.println();
        System.out.println("When no parameters are given, the default configuration file");
        System.out.println("is used (" + DEFAULT_CONFIG_FILE + ").");
        System.out.println("If paramaters are given, default values from a configuration");
        System.out.println("file can be used with the -c flag.");
        System.out.println();
        System.out.println("Example:");
        System.out.println(programName
                + " -c settings/default \"TestExample.run = yes; TestExample.objects = 100\"");
    }

    /**
     * Retrieves a {@link Test} object from a test identifier.
     * @param testIdentifier
     * @return The corresponding {@link Test} or null if it is not found.
     */
    public static Test getTest(String testIdentifier) {
        for (Test test : tests) {
            if (test.getIdentifier().equals(testIdentifier)) {
                return test;
            }
        }
        return null;
    }

    /**
     * Retrieves a {@link Reporter} object from a reporter identifier.
     * @param reporterIdentifier
     * @return The corresponding {@link Reporter} or null if it is not found.
     */
    public static Reporter getReporter(String reporterIdentifier) {
        for (Reporter reporter : reporters) {
            if (reporter.getIdentifier().equals(reporterIdentifier)) {
                return reporter;
            }
        }
        return null;
    }

    /**
     * Parses arguments in order to set up the options/parameters for the
     * various tests. If no arguments are given, the standard config file is
     * used. If options are given, a config file containing default values can
     * be specified with "-c".
     * @param programArgs
     * @return The parsed properties, or null if the input doesn't make sense.
     */
    public static Properties loadSettings(String[] programArgs) {
        Properties properties = new Properties();
        String configFileName = null;
        // Should the default config be used?
        if (programArgs.length == 0) {
            System.out.println("Using default configuration.");
            configFileName = DEFAULT_CONFIG_FILE;
        }
        // Parse all arguments
        String propertyArguments = "";
        for (int i = 0; i < programArgs.length; ++i) {
            if (programArgs[i].equals("-c")) {
                configFileName = programArgs[++i];
            } else {
                propertyArguments += " " + programArgs[i];
            }
        }
        // Check for config file
        if (configFileName != null) {
            File file = new File(configFileName);
            if (file.exists()) {
                try {
                    FileInputStream configInputStream = new FileInputStream(
                            file);
                    properties.load(configInputStream);
                } catch (Exception e) {
                    System.err.println("Could not read configuration file "
                            + configFileName);
                    return null;
                }
            } else {
                System.err.println("Could not find configuration file "
                        + configFileName);
                return null;
            }
        }
        // Parse the extra settings specified on the command line
        String[] extraProperties = propertyArguments.split(";");
        for (String extraProperty : extraProperties) {
            if (extraProperty.trim().length() == 0) {
                continue;
            }
            String[] extraPropertyParts = extraProperty.split("=");
            if (extraPropertyParts.length != 2) {
                System.err.println("Syntax error on parameter: "
                        + extraProperty);
                return null;
            }
            properties.setProperty(extraPropertyParts[0].trim(),
                    extraPropertyParts[1].trim());
        }
        return properties;
    }
}
