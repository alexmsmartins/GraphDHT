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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.benchmark.reporters.Reporter;

/**
 * Class that should run one test. All it requires is the test identifier and
 * the parameters on global format (as they are passed to the benchmark system).
 * @author Patrik
 */
public class TestRunner
{
    Properties parameters;
    String testIdentifier;

    /**
     * @param parameters
     *            Parameters on global format. That means all test specific
     *            options starts with "testIdentifier.".
     * @param testIdentifier
     */
    public TestRunner( Properties parameters, String testIdentifier )
    {
        this.parameters = parameters;
        this.testIdentifier = testIdentifier;
    }

    /**
     * Returns true if a parameter should not be passed to the test.
     * @param parameterName
     * @return
     */
    protected boolean globalParameter( String parameterName )
    {
        if ( parameterName == null )
        {
            return true;
        }
        if ( parameterName.equals( "reporters" ) )
        {
            return true;
        }
        if ( parameterName.endsWith( ".run" ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Runs the test.
     * @return The total score for the test.
     */
    public double Run()
    {
        System.out.println( "Running test: " + testIdentifier );
        // Constant parameters
        Map<String,String> testParameters = new HashMap<String,String>();
        // Parameters with several values. The test should be run once with
        // every value.
        Map<String,String[]> variableParameters = new HashMap<String,String[]>();
        // The number of variations/values in variable parameters.
        int variableParameterVariations = 0;
        for ( Object parameterNameObject : parameters.keySet() )
        {
            String parameterName = (String) parameterNameObject;
            String parameterValue = parameters.getProperty( parameterName )
                .trim();
            if ( globalParameter( parameterName ) )
            {
                continue;
            }
            // Shave off the testIdentifier prefix from all the parameters for
            // this test.
            if ( parameterName.startsWith( testIdentifier + "." ) )
            {
                parameterName = parameterName.substring( testIdentifier
                    .length() + 1 );
            }
            if ( parameterValue.contains( "," ) )
            {
                String[] values = parameterValue.split( "," );
                for ( int i = 0; i < values.length; ++i )
                {
                    values[i] = values[i].trim();
                }
                variableParameters.put( parameterName, values );
                if ( variableParameterVariations != 0
                    && variableParameterVariations != values.length )
                {
                    throw new RuntimeException(
                        "Several varable parameters with different number of variations in test "
                            + testIdentifier );
                }
                variableParameterVariations = values.length;
            }
            else
            {
                testParameters.put( parameterName, parameterValue );
            }
        }
        // Get the test
        Test test = Benchmark.getTest( testIdentifier );
        if ( test == null )
        {
            throw new RuntimeException( "Couldnt find test class for test "
                + testIdentifier );
        }
        // Find out what reporters should be used
        List<Reporter> reporters = new LinkedList<Reporter>();
        String reportersString = parameters.getProperty( "reporters" );
        if ( reportersString != null )
        {
            String[] reporterIdentifiers = reportersString.split( "," );
            for ( String reporterIdentifier : reporterIdentifiers )
            {
                Reporter reporter = Benchmark.getReporter( reporterIdentifier
                    .trim() );
                if ( reporter == null )
                {
                    throw new RuntimeException(
                        "No reporter found with identifier "
                            + reporterIdentifier.trim() );
                }
                reporters.add( reporter );
            }
        }
        // Create reports
        for ( Reporter reporter : reporters )
        {
            System.out
                .println( " Preparing report " + reporter.getIdentifier() );
            try
            {
                reporter.createReport( "./var/benchlogs/", test );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                throw new RuntimeException(
                    "Unable to create report, do you have write access?" );
            }
        }
        OperationCounterSum opSum = new OperationCounterSum();
        // Run the test with all variations of parameters
        for ( int variableParameterIndex = 0; variableParameterIndex < variableParameterVariations
            || (variableParameterVariations == 0 && variableParameterIndex == 0); ++variableParameterIndex )
        {
            // Put the variable parameters into testParameters
            for ( String parameterName : variableParameters.keySet() )
            {
                testParameters.put( parameterName, variableParameters
                    .get( parameterName )[variableParameterIndex] );
            }
            // Output what variable parameters are used
            for ( String parameterName : variableParameters.keySet() )
            {
                System.out.print( " " + parameterName + "="
                    + testParameters.get( parameterName ) );
            }
            // If something was printed, end the line
            if ( variableParameters.keySet().size() > 0 )
            {
                System.out.println();
            }
            // Init the test
            NeoService neo = new EmbeddedNeo( "var" );
            Map<String,OperationCounter> opCounters = new HashMap<String,OperationCounter>();
            test.Init( testParameters, neo );
            // For all subtests...
            for ( String subTestIdentifier : test.getSubTestIdentifiers() )
            {
                System.out.println( " Running " + testIdentifier + " "
                    + subTestIdentifier );
                OperationCounter operationCounter = new OperationCounterImpl();
                // Go!
                operationCounter.timeBegin();
                test.runSubTest( subTestIdentifier, operationCounter );
                operationCounter.timeEnd();
                opCounters.put( subTestIdentifier, operationCounter );
                opSum.addCounter( operationCounter );
            }
            test.TearDown();
            neo.shutdown();
            // Create reports
            for ( Reporter reporter : reporters )
            {
                System.out.println( " Generating report "
                    + reporter.getIdentifier() );
                try
                {
                    reporter.appendToReport( testParameters, opCounters );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                    throw new RuntimeException(
                        "Unable to create report, do you have write access?" );
                }
            }
        }
        // Close reports
        for ( Reporter reporter : reporters )
        {
            try
            {
                reporter.finishReport();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                throw new RuntimeException(
                    "Unable to create report, do you have write access?" );
            }
        }
        return opSum.getScore();
    }
}
