package org.mockit.mock.tcp

import java.net.Socket
import java.util.concurrent.Future

import org.mockit.core._
import org.mockit.logging.LogChannel
import org.mockit.mock.MockUnit

/**
 * Callable instance of [[org.mockit.core.MockServer]] which submits
 * [[org.mockit.mock.tcp.TCPMockWorker]].
 *
 * @param config
 *              mock unit configuration
 * @param mockClass
 *              mock unit
 * @see [[org.mockit.core.MockServer]]
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
