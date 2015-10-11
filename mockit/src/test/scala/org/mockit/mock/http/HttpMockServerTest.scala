package org.mockit.mock.http

import java.io.{BufferedReader, InputStreamReader, DataOutputStream}
import java.net.{InetAddress, Socket}
import java.util.concurrent.{CountDownLatch, Future, Executors}

import scala.collection.mutable.ListBuffer

import org.junit.{After, Before, Test}
import org.junit.Assert._

import org.mockit.{MockItBasicTest, MockIt}
import org.mockit.core.{MockUnitContainer, ConnectionType, ServerConfiguration, ShutdownLatch}
import org.mockit.logging.LogEntry
import org.mockit.util.testmock.TestHttpServerMockUnit

class HttpMockServerTest extends MockItBasicTest {

    import org.mockit.networkclassloader.ClassConverter._

    val pool        = Executors.newSingleThreadExecutor
    val mock        = classOf[TestHttpServerMockUnit]
    val container   = new MockUnitContainer(
        mock.getCanonicalName,
        mock,
        new ServerConfiguration(
            8080,
            1,
            ConnectionType.http
        )
    )

    var shutdown:   ShutdownLatch                   = null
    var latch:      CountDownLatch                  = null
    var logFutures: Future[ListBuffer[LogEntry]]    = null

    @Before def init(): Unit = {
        latch       = new ShutdownLatch
        shutdown    = new ShutdownLatch
        logFutures  = pool.submit(MockIt.mockLocal(container :: Nil, shutdown, latch))

        latch.await()
    }

    @After  def delete(): Unit = {
        shutdown.close
        val logs = logFutures.get

        for (log <- logs) {
            println(log.toString)
            log.exception match {
                case Some(e)    => e.printStackTrace()
                case None       =>
            }
        }
        assertTrue(logs.isEmpty)
    }

    @Test def testRead(): Unit = {
        -- ("testRead")

        val client      = new Socket(InetAddress.getByName("localhost"), 8080)
        val outStream   = new DataOutputStream(client.getOutputStream)

        outStream.writeBytes("GET /test HTTP/1.1\r\n")
        outStream.writeBytes("user: test-user\r\n")
        outStream.writeBytes("\r\n")
        outStream.flush()

        val inStream = new BufferedReader(new InputStreamReader(client.getInputStream))

        var line = inStream.readLine

        while (line != null && line != "") {
            line = inStream.readLine
        }
    }

    @Test def testReadAndWrite(): Unit = {
        -- ("testReadAndWrite")

        val client = new Socket(InetAddress.getByName("localhost"), 8080)

        val outStream = new DataOutputStream(client.getOutputStream)

        outStream.writeBytes("POST /test HTTP/1.1\r\n")
        outStream.writeBytes("Content-Type: text/plain\r\n")
        outStream.writeBytes("Content-Length: 1\r\n")
        outStream.writeBytes("user: test-user\r\n")
        outStream.writeBytes("\r\n")
        outStream.writeBytes("1")
        outStream.flush()

        val inStream = new BufferedReader(new InputStreamReader(client.getInputStream))

        var line = inStream.readLine

        while (line != null && line != "") {
            line = inStream.readLine
        }

        val result = new Array[Char](1)
        inStream.read(result, 0, 1)

        assertEquals(2, result.mkString.toInt)
    }

}
