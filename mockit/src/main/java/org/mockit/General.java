package org.mockit;

import org.mockit.core.*;

/**
 * @author  pheymann
 * @version 0.1.0
 */
public final class General {

    public static final int     DEF_PORT                    = package$.MODULE$.DEFAULT_PORT();
    public static final int     DEF_THREAD_NUM              = package$.MODULE$.DEFAULT_THREAD_NUM();
    public static final int     DEF_REPETITIONS             = package$.MODULE$.DEFAULT_REPETITIONS();

    public static final String  DEF_IP                      = package$.MODULE$.DEFAULT_IP();
    public static final String  DEF_ENC                     = package$.MODULE$.DEFAULT_ENC();

    public enum JConnectionType {

        udp,
        tcp,
        http,
        none

    }

    public enum JBasicMockType {

        client,
        server,
        agent,
        p2p,
        none

    }

    public enum JFaultLevel {

        noFault,
        fixedDelay,
        looseResponse,
        looseConnection,
        multipleResponses

    }

}
