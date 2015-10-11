package org.mockit.mock.http

import java.net.Socket
import java.util.concurrent.Future

import org.mockit.core._
import org.mockit.logging.LogChannel
import org.mockit.mock.MockUnit

/**
 * Callable instance of [[org.mockit.core.MockServer]] which submits
 * [[org.mockit.mock.http.HttpMockWorker]].
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
class HttpMockServer(
                        override val config:     Configuration,
                        override val mockClass:  Class[_ <: MockUnit]
                    ) extends MockServer(config, mockClass) {

    override protected val mockName = classOf[HttpMockServer].getSimpleName

    override def handle(connection: Socket): Future[LogChannel] = {
        val mock = mockClass.newInstance.asInstanceOf[HttpServerMockUnit]

        pool.submit(new HttpMockWorker(connection, mock))
    }

}
