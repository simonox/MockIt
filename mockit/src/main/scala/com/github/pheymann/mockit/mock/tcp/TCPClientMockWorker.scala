package com.github.pheymann.mockit.mock.tcp

import java.net.Socket

import com.github.pheymann.mockit.core.Configuration

/**
 * Subclass of [[com.github.pheymann.mockit.mock.tcp.TCPMockWorker]] which initializes
 * a socket connection to a target server on start up.
 *
 * @param config
 * @param mock
 *              actual instance
 *
 * @author  pheymann
 * @version 0.1.0
 */
class TCPClientMockWorker (
                            val config:         Configuration,

                            override val mock:  TCPMockUnit
                          ) extends TCPMockWorker(
    new Socket(config.targetIp, config.targetPort),
    mock,
    config.mockType
)
