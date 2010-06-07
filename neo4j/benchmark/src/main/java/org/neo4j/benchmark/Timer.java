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

/**
 * A timer/stopwatch based on the System.nanoTime() call.
 * @author Patrik Larsson
 */
public class Timer
{
    // previouslyAccumulatedNanos is used to store old time when pausing.
    protected long previouslyAccumulatedNanos = 0;
    protected long startTime = 0, stopTime = 0;
    // when active is true, stopTime is undefined.
    protected boolean active = false;

    /**
     * Resets the timer. The accumulated time will be set to zero and the timer
     * will be stopped.
     */
    public void Reset()
    {
        startTime = 0;
        stopTime = 0;
        previouslyAccumulatedNanos = 0;
        active = false;
    }

    /**
     * Starts the timer
     */
    public void Start()
    {
        if ( !active )
        {
            startTime = System.nanoTime() - previouslyAccumulatedNanos;
        }
        active = true;
    }

    /**
     * Stops/Pauses the timer. Next time Start is called the timer will continue
     * accumulating time.
     */
    public void Stop()
    {
        if ( active )
        {
            stopTime = System.nanoTime();
            previouslyAccumulatedNanos = stopTime - startTime;
        }
        active = false;
    }

    /**
     * @return The number of nanoseconds accumulated by this timer.
     */
    public long getAccumulatedNanos()
    {
        if ( active )
        {
            return System.nanoTime() - startTime;
        }
        else
        {
            return stopTime - startTime;
        }
    }

    /**
     * @return The number of milliseconds accumulated by this timer.
     */
    public long getAccumulatedMillis()
    {
        return getAccumulatedNanos() / 1000000;
    }

    @Override
    public String toString()
    {
        return getAccumulatedMillis() + "ms";
    }
}
