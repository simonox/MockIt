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

    /**
     * Creates a non-specialized configuration.
     *
     * @param threadNumber
     *          number of threads running on the mock-daemon
     * @param repetitions
     *          number of mock process repetitions
     * @param serverPort
     *          port of the mock-server (if a server is available)
     * @param targetPort
     *          port of the target service (server)
     * @param targetIp
     *          ip of the target service (server)
     * @param originIp
     *          ip of the developers application
     * @param mockNumber
     *          number of mock-daemons running the mock unit
     * @param mockType
     *          is it a client, server, p2p
     * @param mockConnection
     *          is it tcp, udp, or http
     * @return configuration
     */
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

    /**
     * Creates a server specific configuration.
     *
     * @param threadNumber
     *          number of threads running on the mock-daemon
     * @param serverPort
     *          port of the mock-server (if a server is available)
     * @param mockConnection
     *          is it tcp, udp, or http
     * @return server-configuration
     */
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

    /**
     * Creates a client specific configuration.
     *
     * @param threadNumber
     *          number of threads running on the mock-daemon
     * @param repetitions
     *          number of mock process repetitions
     * @param targetPort
     *          port of the target service (server)
     * @param targetIp
     *          ip of the target service (server)
     * @param mockNumber
     *          number of mock-daemons running the mock unit
     * @param mockConnection
     *          is it tcp, udp, or http
     * @return client-configuration
     */
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

    /**
     * Creates udp-p2p specific configuration.
     *
     * @param threadNumber
     *          number of threads running on the mock-daemon
     * @param repetitions
     *          number of mock process repetitions
     * @param serverPort
     *          port of the mock-server (if a server is available)
     * @param targetPort
     *          port of the target service (server)
     * @param targetIp
     *          ip of the target service (server)
     * @param mockNumber
     *          number of mock-daemons running the mock unit
     * @return udp-p2p-configuration
     */
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
