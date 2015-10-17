package com.github.pheymann.mockit.mock.tcp

import java.net.{ServerSocket, Socket}
import java.util.concurrent.{Callable, Executors, TimeUnit}

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core.BasicMockType
import com.github.pheymann._
import com.github.pheymann.mockit.core._

/**
 * Test tcp server which stores the expected client request
 * and checks it against the actual requests.
 *
 * @param testRequest
 *              expected request
 * @param faultLevel
 *              expected fault level
 * @param requestNumber
 *
 * @author  pheymann
 * @version 0.1.0
 */
class TCPTestServer(
                    val testRequest:    Array[Byte],
                    val faultLevel:     FaultLevel,
                    val requestNumber:  Int
                ) extends Callable[Boolean] {

    val server = new ServerSocket(DEFAULT_PORT)

    override def call: Boolean = {
        var failure = false

        try {
            val client = server.accept

            val request = new Array[Byte](testRequest.length)

            client.setSoTimeout(500)

            for (i <- 0 until requestNumber) {
                val error = client.getInputStream.read(request, 0, testRequest.length)

                failure ||=  (error < 0) || !checkMessage(request, testRequest)
            }
        }
        catch {
            case e: Exception =>
                faultLevel match {
                    case fault @ (_: NoFault | _: MultipleResponses) =>
                        //fail(e.getMessage)
                    case _ => /* do nothing */
                }
                failure = true
        }
        finally {
            server.close()
        }
        failure
    }

    def checkMessage(expected: Array[Byte], actual: Array[Byte]): Boolean = {
        var valid = true

        if (actual.length != expected.length) {
            valid = false
        }
        else {
            for (i <- expected.indices) {
                if (!actual(i).equals(expected(i))) {
                    println("message value invalid: %s".format(testRequest(i)))
                    valid &&= actual(i).equals(expected(i))
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
object TCPMockWorkerTest {

    val delay = 1500
    val factor = 2

    val testMessage = Array[Byte](1, 2, 3, 4)

    def runTest(
                   mock: TCPMockUnit,
                   level: FaultLevel,
                   logSize: Int = 0,
                   responseNum: Int = 1
               ): Unit = {

        val server = new TCPTestServer(testMessage, level, responseNum)

        val worker = Executors.newFixedThreadPool(2)
        val failure = worker.submit(server)

        val client = new Socket(DEFAULT_IP, DEFAULT_PORT)
        val log = worker.submit(new TCPMockWorker(client, mock, BasicMockType.client))
        val channel = log.get(10, TimeUnit.SECONDS)

        if (channel.size > 0)
            channel.print()
        //assertFalse(channel.size > 0)
        level match {
            case fault @ (_: NoFault | _: MultipleResponses) => //assertFalse(failure.get(10, TimeUnit.SECONDS))
            case _ => //assertTrue(failure.get(10, TimeUnit.SECONDS))
        }
        //assertEquals(logSize, channel.size)

        client.close()
        worker.shutdown()
        worker.awaitTermination(1, TimeUnit.MINUTES)
    }

}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class TCPMockWorkerTest extends MockItSpec {

    import TCPMockWorkerTest._

    /*
    @Test def testWorkerNonFaulty(): Unit = {
        -- ("testWorkerNonFaulty")
        runTest(new TestTCPMock, NoFault())
    }

    @Test def testWorkerFaultyDelay(): Unit = {
        -- ("testWorkerFaultyDelay")
        runTest(new TestTCPMockFixedDelay, FixedDelay(delay))
    }

    @Test def testWorkerFaultyLooseConnection(): Unit = {
        -- ("testWorkerFaultyLooseConnection")
        runTest(new TestTCPMockLooseConnection, LooseConnection())
    }

    @Test def testWorkerFaultyMultipleResponses(): Unit = {
        -- ("testWorkerFaultyMultipleResponses")
        runTest(new TestTCPMockMultipleResponses, MultipleResponses(factor), responseNum = factor)
    }
    */
}
