package com.github.pheymann.mockit.mock.http

import java.io.{DataOutputStream, InputStreamReader, BufferedReader}
import java.net.ServerSocket
import java.util.concurrent.{Future, CountDownLatch, Executors}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.{MockIt, MockItSpec}
import com.github.pheymann.mockit.logging.LogEntry
import com.github.pheymann.mockit.core.{ShutdownLatch, ConnectionType}
import com.github.pheymann.mockit.networkclassloader.ClassConverter
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.util.testmock.TestHttpClientMockUnit

/**
 * @author  pheymann
 * @version 0.1.0
 */
class HttpClientMockSpec extends MockItSpec {

    import ClassConverter._

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

    before {
        server = new ServerSocket(8080)

        latch       = new ShutdownLatch
        shutdown    = new ShutdownLatch
        logFutures  = pool.submit(MockIt.mockLocal(container :: Nil, shutdown, latch))

        latch.await()
    }

    after {
        shutdown.close
        val logs = logFutures.get

        for (log <- logs) {
            println(log.toString)
            log.exception match {
                case Some(e)    => e.printStackTrace()
                case None       =>
            }
        }

        logs.isEmpty should be (true)
        server.close()
    }

    "A HttpClientMock" should "send HTTP requests to HTTP server and process their responses" in {
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
