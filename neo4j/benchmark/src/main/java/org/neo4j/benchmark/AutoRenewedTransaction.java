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

import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

/**
 * Class that wraps a transaction and automatically renews it
 * (success,finish,beginTx) with a given interval.
 * @author Patrik
 */
public class AutoRenewedTransaction implements Transaction
{
    Transaction tx;
    NeoService neo;
    long renewInterval;
    long counter = 0;

    /**
     * @param neo
     *            Neo instance used to create underlying transactions.
     * @param renewInterval
     *            The interval specifying how often the underlying transaction
     *            will be renewed.
     */
    public AutoRenewedTransaction( NeoService neo, long renewInterval )
    {
        this.neo = neo;
        this.renewInterval = renewInterval;
        tx = neo.beginTx();
    }

    /**
     * Call this for every operation. Every renewInterval:th call the
     * transaction will be renewed.
     */
    public void update()
    {
        if ( ++counter >= renewInterval )
        {
            counter = 0;
            tx.success();
            tx.finish();
            tx = neo.beginTx();
        }
    }

    /**
     * @see org.neo4j.api.core.Transaction#failure()
     */
    public void failure()
    {
        throw new RuntimeException(
            "Trying to fail a possibly renewed transaction (previous parts have been finished as success)." );
    }

    /**
     * @see org.neo4j.api.core.Transaction#finish()
     */
    public void finish()
    {
        tx.finish();
    }

    /**
     * @see org.neo4j.api.core.Transaction#success()
     */
    public void success()
    {
        tx.success();
    }
}
