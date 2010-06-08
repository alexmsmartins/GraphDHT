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

import java.io.IOException;
import java.util.Map;

import org.neo4j.benchmark.OperationCounter;
import org.neo4j.benchmark.Test;

/**
 * Interface for the reporters, objects that can generate reports from the
 * measurements made with {@link OperationCounter}s.
 * @author Patrik
 */
public interface Reporter
{
    /**
     * @return A string identifying this particular reporter.
     */
    public String getIdentifier();

    /**
     * Creates the report.
     * @param outPutDirectory
     *            Directory to put the report in. Created if not existing.
     * @param test
     *            The test that has been performed. (Mainly used to retrieve
     *            testIdentifiers).
     */
    public void createReport( String outPutDirectory, Test test )
        throws IOException;

    /**
     * Appends data to a report from a number of {@link OperationCounter}s. This
     * data should represent one run with one set of parameters of one test
     * including the subtests.
     * @param testParameters
     *            The parameters used in the test, at least those important
     *            enough to include in the report.
     * @param opCounters
     *            A {@link Map} mapping from subtest identifiers to the measured
     *            results.
     * @throws IOException
     */
    public void appendToReport( Map<String,String> testParameters,
        Map<String,OperationCounter> opCounters ) throws IOException;

    /**
     * Cleanup method called in order to close all open files etc.
     * @throws IOException
     */
    public void finishReport() throws IOException;
}
