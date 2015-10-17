package com.github.pheymann.mockit.network

import java.io.ObjectOutputStream
import java.net.Socket

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.logging.{LogEntry, LogData, NetworkLogger}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.core.core._
import NetworkLogger.LogLevel

/**
 * @author  pheymann
 * @version 0.1.0
 */
class LogServerSpec extends MockItSpec {

    val testMock    = "test-mock"
    val testMessage = "test-msg"

    "A LogServer" should
        """wait for log messages from MockAgents which run MockUnits
          |from the origin application. The server stops when he received
          |all log streams. The total number of stream is equal to the
          |number of agents which were acquired.
          |
        """.stripMargin in {

        val agentNumber = 1
        val logs        = new ListBuffer[LogEntry]

        val server = LogServer.init(logs, agentNumber)

        val client          = new Socket(DEFAULT_IP, MOCKIT_LOG_PORT)
        val logOutStream    = new ObjectOutputStream(client.getOutputStream)

        /* number of messages */
        logOutStream.writeInt(1)
        /* log messages */
        logOutStream.writeObject(
            new LogEntry(
                level       = LogLevel.error,
                data        = new LogData(mock = testMock),
                exception   = None,
                msg         = testMessage
            )
        )
        logOutStream.flush()
        client.close()

        server.join()

        logs.nonEmpty should be (true)
        logs.head.level should be (LogLevel.error)
        logs.head.data.mock should be (testMock)
        logs.head.msg should be (testMessage)
    }

}