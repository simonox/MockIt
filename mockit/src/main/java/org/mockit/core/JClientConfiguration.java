package org.mockit.core;

import org.mockit.General.*;

/**
 * Java wrapper for {@link org.mockit.core.ClientConfiguration}. For a detailed
 * description of the parameter use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JClientConfiguration extends JConfiguration {

    /**
     * Initializes the client configuration of a {@link org.mockit.mock.MockUnit}.
     *
     * @param threadNumber
     *                  number of client threads on a [[org.mockit.core.MockAgent]]
     * @param repetitions
     *                  number of repetitions of the client behaviour on a [[org.mockit.core.MockAgent]]
     * @param targetPort
     *                  port of the server which the client has to contact
     * @param targetIp
     *                  ip of the server which the client has to contact
     * @param mockNumber
     *                  number of [[org.mockit.core.MockAgent]]s running with this client
     * @param mockConnection
     *                  connection type, e.g. tcp, http, ...
     */
    public JClientConfiguration(
        final int               threadNumber,
        final int               repetitions,
        final int               targetPort,
        final String            targetIp,
        final int               mockNumber,
        final JConnectionType   mockConnection
    ) {
        this.setThreadNumber(threadNumber);
        this.setRepetitions(repetitions);
        this.setTargetPort(targetPort);
        this.setTargetIp(targetIp);
        this.setMockNumber(mockNumber);
        this.setMockType(JBasicMockType.client);
        this.setMockConnection(mockConnection);
    }

}
