package org.mockit.core

import java.net.Socket
import java.util.concurrent.{TimeoutException, CountDownLatch}

import scala.collection.mutable.ListBuffer

import org.mockit.logging.{Logger, LogEntry}
import org.mockit.mock.MockUnit
import org.mockit.network.{ShutdownServer, UploadServer, LogServer}
import org.mockit.network.InvitationServer

/**
 * Factory singleton to create local or network based '''MockIt''' applications.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object MockFactory {

    /**
     * Creates and runs a local instance of the [[org.mockit.core.MockAgent]].
     *
     * @param mockUnits
     *                  collection of [[org.mockit.mock.MockUnit]]s and their
     *                  [[org.mockit.core.Configuration]]s
     * @param serverShutdown
     *                  control signal to shutdown the instance
     * @param latch
     *                  indicator if all subsystems e.g. server are online
     * @throws org.mockit.core.MockItException
     *                  if an exception occurs during the execution
     * @return collection of all log entries collected during the
     *         execution
     */
    @throws(classOf[MockItException])
    def local(
                mockUnits:      List[MockUnitContainer],
                serverShutdown: ShutdownLatch,
                latch:          CountDownLatch
             ): ListBuffer[LogEntry] = {
        val log     = new Logger {override lazy val name = "MockFactory.local"}
        val logs    = new ListBuffer[LogEntry]

        val agentNumber = countAgentNumber(mockUnits)

        try {
            log > s"initialize log/shutdown-server and mock agents[$agentNumber]"
            val shutdownServer  = ShutdownServer.init(serverShutdown)
            val logServer       = LogServer.init(logs, agentNumber)

            for {
                mockUnit    <- mockUnits
                mockCounter <- 0 until mockUnit.config.mockNumber
            } {
                val mockClass = Class.forName(mockUnit.mockName).asInstanceOf[Class[_ <: MockUnit]]

                MockAgent.init(mockUnit.config, mockClass)

                if (mockUnit.config.mockType.equals(BasicMockType.server))
                    waitUntilOnline(DEFAULT_IP, mockUnit.config.serverPort)

                latch.countDown()
            }

            log > "wait until the log/shutdown-server finished"
            logServer.join()
            shutdownServer.join()
        }
        catch {
            case e: Exception => throw new MockItException(e)
        }
        logs
    }

    /**
     * Creates and runs an local instance of the [[org.mockit.core.MockAgent]]
     * for network purposes.
     *
     * @param config
     *              configuration of the [[org.mockit.mock.MockUnit]]
     * @param mockClass
     *              class of the [[org.mockit.mock.MockUnit]]
     */
    def networkClient(config: Configuration, mockClass: Class[_ <: MockUnit]): Unit = {
        val agent = MockAgent.init(config, mockClass)

        agent.join()
    }

    /**
      * Distributes and manages a network mock up. Necessary
      * ''ByteCode'' is distributed too.
      *
      * Requirements:
      *  - all necessary [[org.mockit.core.MockAgent]]s are available
      *
      * Server-Instances:
      *  - [[org.mockit.network.UploadServer]]
      *  - [[org.mockit.network.InvitationServer]]
      *  - [[org.mockit.network.LogServer]]
      *  - [[org.mockit.network.ShutdownServer]]
      *
      * @param mockUnits
      *                  collection of [[org.mockit.mock.MockUnit]]s and their
      *                  [[org.mockit.core.Configuration]]s
      * @param components
      *                  all depending class definitions used by the [[org.mockit.mock.MockUnit]]s
      * @param serverIps
      *                  holds all ips, of the [[org.mockit.mock.MockUnit]]s which behave as
      *                  server, after system start up
      * @param serverShutdown
      *                  control signal to shutdown the instance
      * @param latch
      *                  indicator if all subsystems e.g. server are online
      * @throws org.mockit.core.MockItException
      *                  if an exception occurs during the execution
      * @return collection of all log entries collected during the
      *         execution
      */
    @throws(classOf[MockItException])
    def networkServer(
                        mockUnits:      List[MockUnitContainer],
                        components:     Map[String, Array[Byte]],
                        serverIps:      ListBuffer[(String, String, Int)],
                        serverShutdown: ShutdownLatch,
                        latch:          CountDownLatch
                     ): ListBuffer[LogEntry] = {
        val log     = new Logger {override lazy val name = "MockFactory.networkServer"}
        val logs    = new ListBuffer[LogEntry]

        val agentNumber = countAgentNumber(mockUnits)

        val mcShutdown = new ShutdownLatch

        try {
            log > "initialize upload server"
            val uploadServer = UploadServer.init(mockUnits, components, serverIps, mcShutdown)
            log > "initialize invitation server"
            val inviteServer = InvitationServer.init(mcShutdown)
            log > "initialize log server"
            val logServer = LogServer.init(logs, agentNumber)
            log > "initialize shutdown server"
            val shutdownServer = ShutdownServer.init(serverShutdown)

            log > "wait until all mocks are distributed"
            uploadServer.join()

            latch.countDown()

            log > "wait until all server finished"
            inviteServer.join()
            logServer.join()
            shutdownServer.join()
        }
        catch {
            case e: Exception => throw new MockItException(e)
        }
        logs
    }

    @throws(classOf[TimeoutException])
    def waitUntilOnline(targetIp: String, targetPort: Int): Unit = {
        var wait        = true
        var repetitions = 0

        do {
            try {
                Thread.sleep(500)
                new Socket(targetIp, targetPort).close()

                wait = false
            }
            catch {
                case e: Throwable => repetitions += 1
            }
        } while (wait && repetitions < SERVER_WAIT_REPETITIONS)

        if (repetitions == SERVER_WAIT_REPETITIONS)
            throw new TimeoutException("online wait time took too long")
    }

    private def countAgentNumber(mockUnits: List[MockUnitContainer]): Int = {
        var number = 0

        for (mockUnit <- mockUnits) {
            number += mockUnit.config.mockNumber
        }
        number
    }

}
