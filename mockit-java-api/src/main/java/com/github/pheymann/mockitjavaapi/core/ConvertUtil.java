package com.github.pheymann.mockitjavaapi.core;

import scala.Tuple2;
import scala.collection.mutable.ListBuffer;

import java.util.ArrayList;
import java.util.List;

import com.github.pheymann.mockitjavaapi.General;
import com.github.pheymann.mockit.core.BasicMockType;
import com.github.pheymann.mockit.core.ConnectionType;
import com.github.pheymann.mockit.core.FaultLevel;
import com.github.pheymann.mockit.core.*;
import com.github.pheymann.mockit.logging.LogEntry;

/**
 * Collection of functions for java to scala conversion.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class ConvertUtil {

    public static scala.Enumeration.Value javaToScala(final General.JBasicMockType jBasicType){
        if (jBasicType.equals(General.JBasicMockType.client))
            return BasicMockType.client();
        else if (jBasicType.equals(General.JBasicMockType.server))
            return BasicMockType.server();
        else if (jBasicType.equals(General.JBasicMockType.agent))
            return BasicMockType.agent();
        else if (jBasicType.equals(General.JBasicMockType.p2p))
            return BasicMockType.p2p();
        else if (jBasicType.equals(General.JBasicMockType.none))
            return BasicMockType.none();
        else
            return BasicMockType.none();
    }

    public static scala.Enumeration.Value javaToScala(final General.JConnectionType jConType) {
        if (jConType.equals(General.JConnectionType.tcp))
            return ConnectionType.tcp();
        else if (jConType.equals(General.JConnectionType.udp))
            return ConnectionType.udp();
        else if (jConType.equals(General.JConnectionType.http))
            return ConnectionType.http();
        else if (jConType.equals(General.JConnectionType.none))
            return ConnectionType.none();
        else
            return ConnectionType.none();
    }

    public static FaultLevel javaToScala(
        final General.JFaultLevel jFault,
        final JFaultLevelData jData
    ) throws IllegalArgumentException {
        if (jFault.equals(General.JFaultLevel.noFault))
            return new NoFault();
        else if (jFault.equals(General.JFaultLevel.fixedDelay))
            return new FixedDelay(jData.getTime());
        else if (jFault.equals(General.JFaultLevel.looseResponse))
            return new LooseResponse();
        else if (jFault.equals(General.JFaultLevel.looseConnection))
            return new LooseConnection();
        else if (jFault.equals(General.JFaultLevel.multipleResponses))
            return new MultipleResponses((jData.getFactor()));
        else
            throw new IllegalArgumentException("no valid fault level");
    }

    public static Tuple2<byte[], FaultLevel> javaToScala(JMockResult result) {
        return new Tuple2<>(result.getResponse(), javaToScala(result.getFault(), result.getData()));
    }

    public static List<LogEntry> scalaToJava(final ListBuffer<LogEntry> logs) {
        final scala.collection.Iterator<LogEntry> iterator = logs.iterator();

        final List<LogEntry> results = new ArrayList<>();

        while (iterator.hasNext()) {
            results.add(iterator.next());
        }
        return results;
    }

}
