package com.github.pheymann.mockit.mock.tcp

import java.net.Socket

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
object TCPMockServerTest {

    import TCPMockWorkerTest._

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
class TCPMockServerTest extends MockItSpec {

    /*
    @Test def testMock(): Unit = {
        -- ("testMock")

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
        assertFalse(TCPMockServerTest.runTest)

        val channel = logs.get

        assertEquals(0, channel.size)

        shutdown.close
        pool.shutdown()
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }
    */

}
