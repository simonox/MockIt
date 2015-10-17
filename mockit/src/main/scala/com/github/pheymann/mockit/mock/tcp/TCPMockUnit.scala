package com.github.pheymann.mockit.mock.tcp

import java.io.InputStream

import com.github.pheymann.mockit.mock.ProtocolMockUnit
import com.github.pheymann.mockit.mock.ProtocolMockUnit

/**
 * Subclass of [[com.github.pheymann.mockit.mock.MockUnit]] with specialisations for tcp
 * mock units.
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class TCPMockUnit extends ProtocolMockUnit {

    /**
     * Interface method to read incoming data if available.
     *
     * @see [[ProtocolMockUnit.waitOnData()]]
     * @param inStream
     *              standard input stream
     * @throws Exception
     *              my throws any kind of exception, especially io exceptions
     */
    @throws(classOf[Exception])
    def receiveData(inStream: InputStream): Unit

}
