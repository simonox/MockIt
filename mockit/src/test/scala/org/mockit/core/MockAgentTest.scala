package org.mockit.core

import scala.collection.mutable.ListBuffer

import org.junit.Assert._
import org.junit.Test

import org.mockit.MockItBasicTest
import org.mockit.util.testmock.TestTCPMock
import org.mockit.network.LogServer
import org.mockit.logging.LogEntry

/**
 * @author  pheymann
 * @version 0.1.0
 */
class MockAgentTest extends MockItBasicTest {

    @Test def testAgentsBehaviour(): Unit = {
        -- ("testAgentsBehaviour")

        val config = new ServerConfiguration(
            DEFAULT_PORT,
            1,
            ConnectionType.tcp
        )

        val agent       = new MockAgent(config, classOf[TestTCPMock])
        val agentNumber = 1

        val logs = new ListBuffer[LogEntry]

        val onlineTestWorker = new Thread("online-test") {
            override def run(): Unit = {
                MockFactory.waitUntilOnline(DEFAULT_IP, DEFAULT_PORT)
            }
        }
        onlineTestWorker.start()

        val logServer = LogServer.init(logs, agentNumber)

        agent.run()

        logServer.join()

        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })
        assertTrue(logs.isEmpty)
    }

}
