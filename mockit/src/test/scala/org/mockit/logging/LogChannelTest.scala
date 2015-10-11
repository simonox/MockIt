package org.mockit.logging

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

import org.junit.Assert._
import org.junit.Test

import org.mockit.MockItBasicTest
import org.mockit.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
class LogChannelTest extends MockItBasicTest {

    import NetworkLogger.LogLevel._

    @Test def testSendAll(): Unit = {
        -- ("testSendAll")

        val channel = new LogChannel

        val time = System.currentTimeMillis
        val msg = "test message"
        val data = new LogData(time)
        val entry = new LogEntry(error, data, None, msg)

        channel.log(error, data, None, msg)

        val server = new ServerSocket(DEFAULT_PORT)
        val clientWorker = new Runnable {
            override def run(): Unit = {
                val client = new Socket(DEFAULT_IP, DEFAULT_PORT)

                channel send new ObjectOutputStream(client.getOutputStream)
                client.close()
            }
        }
        new Thread(clientWorker).start()

        val client = server.accept
        val inStream = new ObjectInputStream(client.getInputStream)

        assertEquals(1, inStream.readInt)
        assertEquals(1, channel.size)

        val externalEntry = inStream.readObject
        assertEquals(entry, externalEntry)

        server.close()
    }

}
