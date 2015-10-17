package com.github.pheymann.mockit.util

import java.net.ServerSocket
import java.util.concurrent.Callable

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.logging.Logger
import com.github.pheymann.mockit.util.TestUtil._

class DummyServer(
                    val resultNumber:   Int,
                    val expected:       Array[Byte]
                 )  extends Callable[Boolean]
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
            client.close()
        }
        > (stop)

        server.close()
        failure
    }

}
