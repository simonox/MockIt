package com.github.pheymann.mockitjavaapi.core;

import com.github.pheymann.mockitjavaapi.General;
import com.github.pheymann.mockit.core.BasicMockType;
import com.github.pheymann.mockit.core.Configuration;
import com.github.pheymann.mockit.core.ConnectionType;
import com.github.pheymann.mockit.core.core.package$;

/**
 * This factory creates Configurations for server, clients and UDP applications.
 * It is an interface to cover the Scala specific implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class ConfigurationFactory {

    public static Configuration create(
        final int                       threadNumber,
        final int                       repetitions,
        final int                       serverPort,
        final int                       targetPort,
        final String                    targetIp,
        final String                    originIp,
        final int                       mockNumber,
        final General.JBasicMockType mockType,
        final General.JConnectionType   mockConnection
    ) {
        return new Configuration(
            threadNumber,
            repetitions,
            serverPort,
            targetPort,
            targetIp,
            originIp,
            mockNumber,
            ConvertUtil.javaToScala(mockType),
            ConvertUtil.javaToScala(mockConnection)
        );
    }

    public static Configuration createServer(
        final int               threadNumber,
        final int               serverPort,
        final General.JConnectionType mockConnection
    ) {
        return new Configuration(
            threadNumber,
            package$.MODULE$.DEFAULT_REPETITIONS(),
            serverPort,
            package$.MODULE$.STUFFING_INT(),
            package$.MODULE$.STUFFING_STRING(),
            package$.MODULE$.DEFAULT_IP(),
            package$.MODULE$.DEFAULT_MOCK_NUM(),
            BasicMockType.server(),
            ConvertUtil.javaToScala(mockConnection)
        );
    }

    public static Configuration createClient(
        final int                       threadNumber,
        final int                       repetitions,
        final int                       targetPort,
        final String                    targetIp,
        final int                       mockNumber,
        final General.JConnectionType   mockConnection
    ) {
        return new Configuration(
            threadNumber,
            repetitions,
            package$.MODULE$.STUFFING_INT(),
            targetPort,
            targetIp,
            package$.MODULE$.DEFAULT_IP(),
            mockNumber,
            BasicMockType.client(),
            ConvertUtil.javaToScala(mockConnection)
        );
    }

    public static Configuration createUDPP2P(
        final int       threadNumber,
        final int       repetitions,
        final int       serverPort,
        final int       targetPort,
        final String    targetIp,
        final int       mockNumber
    ) {
        return new Configuration(
            threadNumber,
            repetitions,
            serverPort,
            targetPort,
            targetIp,
            package$.MODULE$.DEFAULT_IP(),
            mockNumber,
            BasicMockType.p2p(),
            ConnectionType.udp()
        );
    }

}
