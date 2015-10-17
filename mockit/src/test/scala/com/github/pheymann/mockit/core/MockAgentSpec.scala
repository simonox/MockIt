package com.github.pheymann.mockit.core

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.logging.LogEntry

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.util.testmock.TestTCPMock
import com.github.pheymann.mockit.network.LogServer

/**
 * @author  pheymann
 * @version 0.1.0
 */
class MockAgentSpec extends MockItSpec {

    "A MockAgent" should
        """create, manage and destroy MockUnits, handle the necessary Threads and
          |socket connections
        """.stripMargin in {
        val config = new ServerConfiguration(
            serverPort      = DEFAULT_PORT,
            threadNumber    = 1,
            mockConnection  = ConnectionType.tcp
        )

        val agent       = new MockAgent(config, classOf[TestTCPMock])
        val agentNumber = 1

        val logs = new ListBuffer[LogEntry]

        val onlineTestWorker = new Thread("wait-until-online-test") {
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
        logs.isEmpty should be (true)
    }

}