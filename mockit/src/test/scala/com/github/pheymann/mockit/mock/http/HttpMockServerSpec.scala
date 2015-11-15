package com.github.pheymann.mockit.mock.http

import java.io.{InputStreamReader, BufferedReader, DataOutputStream}
import java.net.{InetAddress, Socket}
import java.util.concurrent.{CountDownLatch, Future, Executors}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.{MockIt, MockItSpec}
import com.github.pheymann.mockit.logging.LogEntry
import com.github.pheymann.mockit.core.ConnectionType
import com.github.pheymann.mockit.networkclassloader.ClassConverter
import com.github.pheymann.mockit.core.{MockUnitContainer, ServerConfiguration, ShutdownLatch}
import com.github.pheymann.mockit.util.testmock.TestHttpServerMockUnit

/**
 * @author  pheymann
 * @version 0.2.0
 */
class HttpMockServerSpec extends MockItSpec {

    import ClassConverter._

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

    before {
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
        logs.isEmpty should be(true)
    }

    "A HttpMockServer" should "receive HTTP request, process them and send HTTP responses to the clients" in {
        val client      = new Socket(InetAddress.getByName("localhost"), 8080)
        val outStream   = new DataOutputStream(client.getOutputStream)

        outStream.writeBytes("GET /test HTTP/1.1\r\n")
        outStream.writeBytes("user: test-user\r\n")
        outStream.writeBytes("\r\n")
        outStream.flush()

        val inStream = new BufferedReader(new InputStreamReader(client.getInputStream))

        var line = inStream.readLine

        while (line != null && !line.isEmpty) {
            line = inStream.readLine
        }
    }

    it should "be able to read data from the request" in {
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

        while (line != null && !line.isEmpty) {
            line = inStream.readLine
        }

        val result = new Array[Char](1)
        inStream.read(result, 0, 1)

        result.mkString.toInt should be (2)
    }

    it should "be able to send error responses if the received request doesn't meet the expectations" in {
        val client = new Socket(InetAddress.getByName("localhost"), 8080)

        val outStream = new DataOutputStream(client.getOutputStream)

        outStream.writeBytes("PUT /test HTTP/1.1\r\n")
        outStream.writeBytes("Content-Type: text/plain\r\n")
        outStream.writeBytes("Content-Length: 1\r\n")
        outStream.writeBytes("user: wrong-user\r\n")
        outStream.writeBytes("\r\n")
        outStream.writeBytes("1")
        outStream.flush()

        val inStream = new BufferedReader(new InputStreamReader(client.getInputStream))

        var line = inStream.readLine

        while (line != null && !line.isEmpty) {
            line = inStream.readLine
        }

        val result = new Array[Char](1)
        inStream.read(result, 0, 1)

        result.mkString.toInt should be (3)
    }

    it should "be able to send a global error response if a receive request doesn't meet the expectations" in {
        val client = new Socket(InetAddress.getByName("localhost"), 8080)

        val outStream = new DataOutputStream(client.getOutputStream)

        outStream.writeBytes("GET /testWrong HTTP/1.1\r\n")
        outStream.writeBytes("Content-Type: text/plain\r\n")
        outStream.writeBytes("Content-Length: 1\r\n")
        outStream.writeBytes("user: test-user\r\n")
        outStream.writeBytes("\r\n")
        outStream.writeBytes("1")
        outStream.flush()

        val inStream = new BufferedReader(new InputStreamReader(client.getInputStream))

        var line = inStream.readLine

        while (line != null && !line.isEmpty) {
            line = inStream.readLine
        }

        val result = new Array[Char](1)
        inStream.read(result, 0, 1)

        result.mkString.toInt should be (4)
    }

}
