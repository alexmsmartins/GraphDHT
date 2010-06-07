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

import java.util.Map;

import org.neo4j.api.core.NeoService;

/**
 * Object that should run one test (possibly containing several subtests) on one
 * data set ("Curcuit" in poleposition). Each subtest can use it's own
 * {@link OperationCounter} etc.
 * @author Patrik
 */
public interface Test
{
    String[] getSubTestIdentifiers();

    String getIdentifier();

    String getDescription();

    /**
     * Method called to prepare the test.
     * @param parameters
     */
    void Init( Map<String,String> parameters, NeoService neo );

    /**
     * Method called after all subtests have been run.
     */
    void TearDown();

    /**
     * Method that should run one specified subtest.
     * @param subTestIdentifier
     *            String identifying the subtest.
     * @param operationCounter
     *            Object used for reporting how many operations the subtest is
     *            performing.
     */
    void runSubTest( String subTestIdentifier, OperationCounter operationCounter );
}
