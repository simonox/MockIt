package org.mockit.mock.tcp

import java.io.InputStream

import org.mockit.mock.ProtocolMockUnit

/**
 * Subclass of [[org.mockit.mock.MockUnit]] with specialisations for tcp
 * mock units.
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class TCPMockUnit extends ProtocolMockUnit {

    /**
     * Interface method to read incoming data if available.
     *
     * @see [[org.mockit.mock.ProtocolMockUnit.waitOnData()]]
     * @param inStream
     *              standard input stream
     * @throws java.lang.Exception
     *              my throws any kind of exception, especially io exceptions
     */
    @throws(classOf[Exception])
    def receiveData(inStream: InputStream): Unit

}
