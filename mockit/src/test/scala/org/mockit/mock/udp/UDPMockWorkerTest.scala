package org.mockit.mock.udp

import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import java.util.concurrent.{Callable, Executors, TimeUnit}

import org.junit.Assert._
import org.junit.Test

import org.mockit.MockItBasicTest
import org.mockit.core._
import org.mockit.logging.Logger
import org.mockit.util.testmock.TestUDPMock

/**
 * Test udp server which stores the expected clients request
 * and checks it against the actual requests.
 *
 * @param testRequest
 *              expected request
 * @param faultLevel
 *              expected fault level
 * @param requestNumber
 */
class UDPTestServer(
                    val testRequest:    Array[Byte],
                    val faultLevel:     FaultLevel,
                    val requestNumber: Int
                )   extends Callable[Boolean]
                    with    Logger {

    val server = new DatagramSocket(DEFAULT_PORT + 1)

    override def call: Boolean = {
        var failure = false

        try {
            val data = new Array[Byte](testRequest.length)
            val dataPackSend = new DatagramPacket(data, 0, data.length, InetAddress.getByName(DEFAULT_IP), DEFAULT_PORT)
            val dataPackRec = new DatagramPacket(data, 0, data.length)

            this >> "send udp package to mock multiple times"
            server.send(dataPackSend)
            server.send(dataPackSend)
            server.send(dataPackSend)

            this >> "receive udp package to mock"
            server.receive(dataPackRec)

            failure = !checkMessage(data, testRequest)
        }
        catch {
            case e: Exception =>
                faultLevel match {
                    case fault @ (_: NoFault | _: MultipleResponses) => fail(e.getMessage)
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
object UDPMockWorkerTest {

    val delay = 1500
    val factor = 2

    val testResponse = Array[Byte](1, 2, 3, 4)

    def runTest(
                   mock:        UDPMockUnit,
                   level:       FaultLevel,
                   logSize:     Int = 0,
                   responseNum: Int = 1
               ): Unit = {

        val server = new UDPTestServer(testResponse, level, responseNum)

        val config = UDPP2PConfiguration(
            targetPort      = DEFAULT_PORT + 1,
            mockNumber      = 1
        )

        val worker = Executors.newFixedThreadPool(2)
        val log = worker.submit(new UDPMockWorker(config, mock))
        val failure = worker.submit(server)

        val channel = log.get(10, TimeUnit.SECONDS)

        if (channel.size > 0)
            channel.print()
        assertFalse(channel.size > 0)
        level match {
            case fault @ (_: NoFault | _: MultipleResponses) => assertFalse(failure.get(10, TimeUnit.SECONDS))
            case _ => assertTrue(failure.get(10, TimeUnit.SECONDS))
        }
        assertEquals(logSize, channel.size)

        worker.shutdown()
        worker.awaitTermination(1, TimeUnit.MINUTES)
    }

}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class UDPMockWorkerTest extends MockItBasicTest {

    import UDPMockWorkerTest._

    @Test def testWorkerNonFaulty(): Unit = {
        -- ("testWorkerNonFaulty")
        runTest(new TestUDPMock, NoFault())
    }

}
