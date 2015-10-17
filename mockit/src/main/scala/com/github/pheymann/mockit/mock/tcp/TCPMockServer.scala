package com.github.pheymann.mockit.mock.tcp

import java.net.Socket
import java.util.concurrent.Future

import com.github.pheymann.mockit.core.MockServer
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.logging.LogChannel

/**
 * Callable instance of [[com.github.pheymann.mockit.core.MockServer]] which submits
 * [[com.github.pheymann.mockit.mock.tcp.TCPMockWorker]].
 *
 * @see [[com.github.pheymann.mockit.core.MockServer]]
 *
 * @param config
 *              mock unit configuration
 * @param mockClass
 *              mock unit
 *
 * @author  pheymann
 * @version 0.1.0
 */
class TCPMockServer (
                        override val config:     Configuration,
                        override val mockClass:  Class[_ <: MockUnit]
                    ) extends MockServer(config, mockClass) {

    protected override val mockName = classOf[TCPMockWorker].getSimpleName

    override def handle(connection: Socket): Future[LogChannel] = {
        val mock = mockClass.newInstance.asInstanceOf[TCPMockUnit]

        pool.submit(new TCPMockWorker(connection, mock, config.mockType))
    }

}
