package org.mockit.util

import java.net.ServerSocket
import java.util.concurrent.Callable

import org.mockit.core._
import org.mockit.util.TestUtil._
import org.mockit.logging.Logger

class NetworkTestServer(
                        val resultNumber:   Int,
                        val expected:       Array[Byte]
                       )    extends Callable[Boolean]
                            with    Logger {

    val server = new ServerSocket(DEFAULT_PORT)

    override def call: Boolean = {
        var failure = false

        > (start)
        for (i <- 0 until resultNumber) {
            val result = new Array[Byte](expected.length)

            val client = server.accept
            val inStream = client.getInputStream

            inStream.read(result, 0, result.length)

            val check = checkArrayEquals(expected, result)

            if (!check) {
                error ("received data not as expected")
                failure = true
            }
            client.close
        }
        > (stop)

        server.close
        failure
    }

}
