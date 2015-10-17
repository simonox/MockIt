package com.github.pheymann.mockit.mock.tcp

import java.net.Socket
import java.util.concurrent.{TimeUnit, Executors}

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.util.testmock.TestTCPMock

/**
 * @author  pheymann
 * @version 0.1.0
 */
object TCPMockServerSpec {

    import TCPMockWorkerSpec._

    def runTest: Boolean = {
        MockFactory.waitUntilOnline(DEFAULT_IP, DEFAULT_PORT)

        val client = new Socket(DEFAULT_IP, DEFAULT_PORT)

        val response = new Array[Byte](testMessage.length)
        val error = client.getInputStream.read(response, 0, response.length)

        var failure = error < 0

        client.close()
        failure = !checkResponse(response, testMessage)
        failure
    }

    def checkResponse(response: Array[Byte], actual: Array[Byte]): Boolean = {
        var valid = true

        if (actual.length != response.length) {
            valid = false
        }
        else {
            for (i <- response.indices) {
                if (!actual(i).equals(response(i))) {
                    println("response value invalid: %s".format(testMessage(i)))
                    valid &&= actual(i).equals(response(i))
                }
            }
        }
        valid
    }

}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class TCPMockServerSpec extends MockItSpec {

    "A TCPMockServer" should "process requests sent via a TCP connection" in {
        val config = new ServerConfiguration(
            DEFAULT_PORT,
            1,
            ConnectionType.tcp
        )

        val shutdown = new ShutdownLatch
        val serverShutdown = new ShutdownLatch

        val server = new TCPMockServer(config, classOf[TestTCPMock])


        val pool = Executors.newSingleThreadExecutor
        val logs = pool.submit(server)

        serverShutdown.close
        TCPMockServerSpec.runTest should be (false)

        val channel = logs.get

        channel.size should be (0)

        shutdown.close
        pool.shutdown()
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }

}
