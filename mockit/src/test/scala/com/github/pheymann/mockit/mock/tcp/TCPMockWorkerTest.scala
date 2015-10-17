package com.github.pheymann.mockit.mock.tcp

import java.net.{ServerSocket, Socket}
import java.util.concurrent.{Callable, Executors, TimeUnit}

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core.BasicMockType
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.util.testmock.{TestTCPMock, TestTCPMockFixedDelay, TestTCPMockLooseConnection, TestTCPMockMultipleResponses}

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
                    case fault @ (_: NoFault | _: MultipleResponses) => e.printStackTrace()
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
                   mock:        TCPMockUnit,
                   level:       FaultLevel,
                   logSize:     Int = 0,
                   responseNum: Int = 1
               ): Boolean = {

        val server = new TCPTestServer(testMessage, level, responseNum)

        val worker = Executors.newFixedThreadPool(2)
        val serverFailure = worker.submit(server)

        val client = new Socket(DEFAULT_IP, DEFAULT_PORT)
        val log = worker.submit(new TCPMockWorker(client, mock, BasicMockType.client))
        val channel = log.get(10, TimeUnit.SECONDS)

        var failure = false

        if (channel.size > 0)
            channel.print()
        failure ||= (channel.size > 0)
        level match {
            case fault @ (_: NoFault | _: MultipleResponses) => failure ||= serverFailure.get(10, TimeUnit.SECONDS)
            case _ => failure ||= !serverFailure.get(10, TimeUnit.SECONDS)
        }
        failure ||= !(logSize == channel.size)

        client.close()
        worker.shutdown()
        worker.awaitTermination(1, TimeUnit.MINUTES)

        failure
    }

}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class TCPMockWorkerTest extends MockItSpec {

    import TCPMockWorkerTest._

    "A TCPMockWorker" should "run a MockUnit without a fault simulation" in {
        TCPMockWorkerTest.runTest(new TestTCPMock, NoFault()) should be (false)
    }

    it should "run a MockUnit with delay simulation" in {
        TCPMockWorkerTest.runTest(new TestTCPMockFixedDelay, FixedDelay(delay)) should be (false)
    }

    it should "run a MockUnit with connection loss simulation" in {
        TCPMockWorkerTest.runTest(new TestTCPMockLooseConnection, LooseConnection()) should be (false)
    }

    it should "run a MockUnit with response multiplication simulation" in {
        TCPMockWorkerTest.runTest(new TestTCPMockMultipleResponses, MultipleResponses(factor), responseNum = factor) should be (false)
    }

}
