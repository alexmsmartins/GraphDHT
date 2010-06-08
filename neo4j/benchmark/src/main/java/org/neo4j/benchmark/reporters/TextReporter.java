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
package org.neo4j.benchmark.reporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.neo4j.benchmark.OperationCounter;
import org.neo4j.benchmark.OperationCounterSum;
import org.neo4j.benchmark.Test;
import org.neo4j.benchmark.OperationCounter.Operation;

public class TextReporter implements Reporter
{
    public String getIdentifier()
    {
        return "TextReporter";
    }

    Test test;
    BufferedWriter out;

    public void createReport( String outPutDirectory, Test test )
        throws IOException
    {
        new File( outPutDirectory ).mkdirs();
        // Open output file
        File outFile = new File( outPutDirectory + "/" + test.getIdentifier() );
        this.test = test;
        out = new BufferedWriter( new FileWriter( outFile ) );
        out.write( "----------------------------------------" );
        out.newLine();
        out.write( "Date: " + new Date() );
        out.newLine();
    }

    public void appendToReport( Map<String,String> testParameters,
        Map<String,OperationCounter> opCounters ) throws IOException
    {
        // Generate reports for subtests
        for ( String subTestIdentifier : test.getSubTestIdentifiers() )
        {
            OperationCounter operationCounter = opCounters
                .get( subTestIdentifier );
            if ( operationCounter == null )
            {
                throw new RuntimeException(
                    "No OperationCounter found for subtest "
                        + test.getIdentifier() + "." + subTestIdentifier );
            }
            appendSubTest( testParameters, subTestIdentifier, operationCounter );
        }
        // Generate total result for all subtests
        OperationCounterSum opSum = new OperationCounterSum();
        for ( String subTestIdentifier : opCounters.keySet() )
        {
            opSum.addCounter( opCounters.get( subTestIdentifier ) );
        }
        appendSubTest( testParameters, null, opSum );
    }

    /**
     * Internal method for creating content for one subtest, or for the entire
     * test if the subtest identifier is null.
     * @param testParameters
     * @param subTestIdentifier
     * @param operationCounter
     * @throws IOException
     */
    private void appendSubTest( Map<String,String> testParameters,
        String subTestIdentifier, OperationCounter operationCounter )
        throws IOException
    {
        if ( subTestIdentifier == null )
        {
            out.write( "Total Results for " + test.getIdentifier() + ":" );
        }
        else
        {
            out.write( "Results for " + test.getIdentifier() + "."
                + subTestIdentifier + ":" );
        }
        out.newLine();
        out.write( "  Parameters: " + testParameters );
        out.newLine();
        out.write( "  Total operations:" );
        out.newLine();
        for ( Operation operation : OperationCounter.Operation.values() )
        {
            long numberOfOperations = operationCounter
                .getNumberOfOperations( operation );
            if ( numberOfOperations == 0 )
            {
                continue;
            }
            out.write( "    " + operation + " " + numberOfOperations );
            out.newLine();
        }
        out.write( "  Time pre-commit: " );
        out.write( (operationCounter.getPreCommitTimeNanos() / 1000000) + "ms" );
        out.newLine();
        out.write( "  Time in-commit:  " );
        out.write( (operationCounter.getInCommitTimeNanos() / 1000000) + "ms" );
        out.newLine();
        out.write( "  Total time:      " );
        out.write( (operationCounter.getTotalTimeNanos() / 1000000) + "ms" );
        out.newLine();
        out.write( "  Operations per second:" );
        out.newLine();
        for ( Operation operation : OperationCounter.Operation.values() )
        {
            long numberOfOperations = operationCounter
                .getNumberOfOperations( operation );
            if ( numberOfOperations == 0 )
            {
                continue;
            }
            long millis = operationCounter.getTotalTimeNanos() / 1000000;
            out.write( "    " + operation + ": " + numberOfOperations * 1000
                / millis + "/s" );
            out.newLine();
        }
        if ( subTestIdentifier == null )
        {
            out.write( "  Total score:" );
        }
        else
        {
            out.write( "  Score:" );
        }
        out.newLine();
        out.write( "    " + (int) operationCounter.getScore() );
        out.newLine();
    }

    public void finishReport() throws IOException
    {
        out.write( "----------------------------------------" );
        out.newLine();
        out.close();
    }
}
