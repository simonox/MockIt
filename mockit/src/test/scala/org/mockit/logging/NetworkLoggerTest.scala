package org.mockit.logging

import java.io.ObjectInputStream
import java.net.ServerSocket

import org.junit.Assert._
import org.junit.{Before, Test}

import org.mockit.core._
import org.mockit.MockItBasicTest

/**
 * @author  pheymann
 * @version 0.1.0
 */
class NetworkLoggerTest extends MockItBasicTest {

    var logger: NetworkLogger = null

    @Before def init(): Unit = {
        logger = new NetworkLogger {
            override protected val mockName = "test-name"
            override protected val mockType = BasicMockType.none.toString
        }
    }

    @Test def testSendAllAndClear(): Unit = {
        -- ("testSendAllAndClear")

        val msg = "test message"

        logger.sendError(msg)

        val server = new ServerSocket(MOCKIT_LOG_PORT)
        val clientWorker = new Thread {
            override def run(): Unit = {
                NetworkLogger.sendLogsAndClear(DEFAULT_IP, logger.channel)
            }
        }
        clientWorker.start()

        val client = server.accept
        val inStream = new ObjectInputStream(client.getInputStream)

        clientWorker.join()
        assertEquals(1, inStream.readInt)
        assertEquals(0, logger.channel.size)

        server.close()
    }

    @Test def testSendAllAndClearZeroLogs(): Unit = {
        -- ("testSendAllAndClearZeroLogs")

        val server = new ServerSocket(MOCKIT_LOG_PORT)
        val clientWorker = new Thread {
            override def run(): Unit = {
                NetworkLogger.sendLogsAndClear(DEFAULT_IP, logger.channel)
            }
        }
        clientWorker.start()

        val client = server.accept
        val inStream = new ObjectInputStream(client.getInputStream)

        clientWorker.join()
        assertEquals(0, inStream.readInt)

        server.close()
    }
    
}
