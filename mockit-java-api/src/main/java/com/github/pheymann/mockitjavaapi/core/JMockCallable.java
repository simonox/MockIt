package com.github.pheymann.mockitjavaapi.core;

import scala.Tuple3;
import scala.collection.Iterator;
import scala.collection.mutable.ListBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.github.pheymann.mockit.logging.LogEntry;

/**
 * Extends the Callable interface to store address of the {@link com.github.pheymann.mockit.mock.MockUnit}s
 * which act as server. These address are set at runtime during the initialization
 * phase.
 *
 * @see     com.github.pheymann.mockit.MockIt
 * @author  pheymann
 * @version 0.1.0
 */
public abstract class JMockCallable implements Callable<List<LogEntry>> {

    protected ListBuffer<Tuple3<String, String, Object>> serverIps = new scala.collection.mutable.ListBuffer<>();

    /**
     * Maps the scala internal address representation into a java.
     *
     * @return list of tuple (mock name, ip, port)
     */
    public final List<Object[]> getServerIps() {
        final Iterator<Tuple3<String, String, Object>> iterator = serverIps.iterator();

        final List<Object[]> results = new ArrayList<>();

        while (iterator.hasNext()) {
            Tuple3<String, String, Object> element = iterator.next();

            results.add(new Object[] {element._1(), element._2(), element._3()});
        }
        return results;
    }

}
