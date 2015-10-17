package com.github.pheymann.mockit.logging

import java.io.ObjectInputStream
import java.net.ServerSocket
import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.core.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
class NetworkLoggerSpec extends MockItSpec {

    var logger: NetworkLogger = _

    before {
        logger = new NetworkLogger {
            override protected val mockName = "test-name"
            override protected val mockType = BasicMockType.none.toString
        }
    }

    "A NetworkLogger" should "send all stored logs to the origin server and clear his cache" in {
        val msg = "test message"

        logger.sendError(msg)
        logger.sendWarn(msg)

        val server          = new ServerSocket(MOCKIT_LOG_PORT)
        val clientWorker    = new Thread {
            override def run(): Unit = {
                NetworkLogger.sendLogsAndClear(DEFAULT_IP, logger.channel)
            }
        }
        clientWorker.start()

        val client      = server.accept
        val logStream   = new ObjectInputStream(client.getInputStream)

        clientWorker.join()

        val numberOfLogs = logStream.readInt()
        val error   = logStream.readObject.asInstanceOf[LogEntry]
        val warn    = logStream.readObject.asInstanceOf[LogEntry]

        numberOfLogs should be (2)

        error.level should be (NetworkLogger.LogLevel.error)
        error.msg should be (msg)

        warn.level should be (NetworkLogger.LogLevel.warn)
        warn.msg should be (msg)

        logger.channel.size should be (0)

        server.close()
    }

    it should "be able to handle zero logs entries too" in {
        val server          = new ServerSocket(MOCKIT_LOG_PORT)
        val clientWorker    = new Thread {
            override def run(): Unit = {
                NetworkLogger.sendLogsAndClear(DEFAULT_IP, logger.channel)
            }
        }
        clientWorker.start()

        val client      = server.accept
        val logStream   = new ObjectInputStream(client.getInputStream)

        clientWorker.join()

        logStream.readInt() should be (0)

        server.close()
    }
    
}