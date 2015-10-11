package org.mockit.core;

/**
 * Java wrapper for {@link org.mockit.core.UDPP2PConfiguration}. For a detailed
 * description use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JUDPP2PConfiguration extends JConfiguration {

    /**
     * Initializes the server configuration of a {@link org.mockit.mock.MockUnit}.
     *
     * @param threadNumber
     *                  number of client threads
     * @param repetitions
     *                  number of repetitions of the client behaviour on a [[org.mockit.core.MockAgent]]
     * @param serverPort
     *                  reads data packages of the port if it is set
     * @param targetPort
     *                  sends data packages to the target port
     * @param targetIp
     *                  sends data packages to the target ip
     * @param mockNumber
     *                  number of [[org.mockit.core.MockAgent]]s running with this client
     */
    public JUDPP2PConfiguration(
        final int       threadNumber,
        final int       repetitions,
        final int       serverPort,
        final int       targetPort,
        final String    targetIp,
        final int       mockNumber
    ) {
        this.setThreadNumber(threadNumber);
        this.setRepetitions(repetitions);
        this.setServerPort(serverPort);
        this.setTargetPort(targetPort);
        this.setTargetIp(targetIp);
        this.setMockNumber(mockNumber);
    }

}
