package com.github.pheymann.mockit.mock.http

import java.net.Socket
import java.util.concurrent.Future

import com.github.pheymann.mockit.core.MockServer
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.logging.LogChannel

/**
 * Callable instance of [[com.github.pheymann.mockit.core.MockServer]] which submits
 * [[HttpMockWorker]].
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
