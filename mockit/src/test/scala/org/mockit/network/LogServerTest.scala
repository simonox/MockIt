package org.mockit.network

import java.io.ObjectOutputStream
import java.net.Socket

import scala.collection.mutable.ListBuffer

import org.junit.Test
import org.junit.Assert._

import org.mockit.MockItBasicTest
import org.mockit.logging.{LogData, LogEntry}
import org.mockit.logging.NetworkLogger.LogLevel
import org.mockit.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
class LogServerTest extends MockItBasicTest {

    val testMock    = "test-mock"
    val testMessage = "test-msg"

    @Test def testCollectLogs(): Unit = {
        -- ("testCollectLogs")

        val logs = new ListBuffer[LogEntry]

        val server = LogServer.init(logs, 1)

        val client = new Socket(DEFAULT_IP, MOCKIT_LOG_PORT)
        val outStream = new ObjectOutputStream(client.getOutputStream)

        outStream.writeInt(1)
        outStream.writeObject(
            new LogEntry(
                LogLevel.error,
                new LogData(mock = testMock),
                None,
                testMessage
            )
        )
        outStream.flush()
        client.close()

        server.join()

        assertTrue(logs.nonEmpty)
        assertEquals(logs.head.level, LogLevel.error)
        assertEquals(logs.head.data.mock, testMock)
        assertEquals(logs.head.msg, testMessage)
    }

}
