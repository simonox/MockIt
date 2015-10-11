package org.mockit.core;

import org.mockit.General;

/**
 * Java wrapper for {@link org.mockit.core.ServerConfiguration}. For a detailed
 * description use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JServerConfiguration extends JConfiguration {

    /**
     * Initializes the server configuration of a {@link org.mockit.mock.MockUnit}.
     *
     * @param serverPort
     *                  server port of the mock application
     * @param threadNumber
     *                  number of threads running to answer incoming
     *                  requests
     * @param mockConnection
     *                  connection type, e.g. tcp, http, ...
     */
    public JServerConfiguration(
        final int               serverPort,
        final int               threadNumber,
        final General.JConnectionType mockConnection
    ) {
        this.setServerPort(serverPort);
        this.setThreadNumber(threadNumber);
        this.setMockType(General.JBasicMockType.server);
        this.setMockConnection(mockConnection);
    }

}
