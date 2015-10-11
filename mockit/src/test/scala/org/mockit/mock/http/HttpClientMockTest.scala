package org.mockit.mock.http

import java.io.{InputStreamReader, BufferedReader, DataOutputStream}
import java.net.ServerSocket
import java.util.concurrent.{Future, CountDownLatch, Executors}

import scala.collection.mutable.ListBuffer

import org.junit.Assert._
import org.junit.{After, Before, Test}

import org.mockit.{MockItBasicTest, MockIt}
import org.mockit.core._
import org.mockit.logging.LogEntry
import org.mockit.util.testmock.TestHttpClientMockUnit

class HttpClientMockTest extends MockItBasicTest {

    import org.mockit.networkclassloader.ClassConverter._

    val pool        = Executors.newSingleThreadExecutor
    val mock        = classOf[TestHttpClientMockUnit]
    val container   = new MockUnitContainer(
        mock.getCanonicalName,
        mock,
        new ClientConfiguration(
            1,
            1,
            8080,
            "127.0.0.1",
            1,
            ConnectionType.http
        )
    )

    var server:     ServerSocket                    = null

    var shutdown:   ShutdownLatch                   = null
    var latch:      CountDownLatch                  = null
    var logFutures: Future[ListBuffer[LogEntry]]    = null

    @Before def init(): Unit = {
        server = new ServerSocket(8080)

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
        server.close()
    }

    @Test def testRead(): Unit = {
        -- ("testRead")

        val client = server.accept

        val inStream = new BufferedReader(new InputStreamReader(client.getInputStream))

        var line = inStream.readLine

        while (line != null && line != "") {
            line = inStream.readLine
        }

        val outStream = new DataOutputStream(client.getOutputStream)

        outStream.writeBytes("HTTP/1.1 " + OK.key + " \r\n")
        outStream.writeBytes("user: test-user\r\n")
        outStream.writeBytes("Content-Type: text/plain\r\n")
        outStream.writeBytes("Content-Length: 11\r\n")
        outStream.writeBytes("\r\n")
        outStream.writeBytes("hello world")
        outStream.flush()
        outStream.close()
    }

}
