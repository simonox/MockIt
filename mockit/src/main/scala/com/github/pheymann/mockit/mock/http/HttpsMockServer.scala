package com.github.pheymann.mockit.mock.http

import java.net.Socket
import java.util.concurrent.Future

import com.github.pheymann.mockit.core.{SSLMockServer, Configuration}
import com.github.pheymann.mockit.logging.LogChannel
import com.github.pheymann.mockit.mock.MockUnit

/**
 * @author  pheymann
 * @version 0.3.0
 */
class HttpsMockServer (
                          override val config:     Configuration,
                          override val mockClass:  Class[_ <: MockUnit]
                      ) extends SSLMockServer(config, mockClass) {

    override protected val mockName = classOf[HttpsMockServer].getSimpleName

    override def handle(connection: Socket): Future[LogChannel] = {
        val mock = mockClass.newInstance.asInstanceOf[HttpServerMockUnit]

        pool.submit(new HttpMockWorker(connection, mock))
    }

}
