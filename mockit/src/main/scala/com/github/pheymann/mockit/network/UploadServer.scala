package com.github.pheymann.mockit.network

import java.net.ServerSocket

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core.BasicMockType
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.core.{MockFactory, MockUnitContainer}
import com.github.pheymann.mockit.logging.Logger
import com.github.pheymann.mockit.networkclassloader.NetworkClassServer

import scala.collection.mutable.ListBuffer

/**
 * @author  pheymann
 * @version 0.1.0
 */
object UploadServer {

    /**
     * Creates and starts a ''Threads'' which runs an instance
     * of [[UploadServer]].
     *
     * @param mockUnits
     *              collection of all [[com.github.pheymann.mockit.mock.MockUnit]]s
     * @param components
     *              components necessary to run the mock units
     * @param serverIps
     *              collection of all [[com.github.pheymann.mockit.core.MockAgent]] address if their
     *              execute a mock server
     * @param shutdown
     *              shutdown signal
     * @return actual thread instance
     */
    def init(
                mockUnits:  List[MockUnitContainer],
                components: Map[String, Array[Byte]],
                serverIps:  ListBuffer[(String, String, Int)],
                shutdown:   ShutdownLatch): Thread = {
        val worker = new Thread(new UploadServer(mockUnits, components, serverIps, shutdown))

        worker.start()
        worker
    }

}

/**
 * Server instance to send class files and configurations to [[com.github.pheymann.mockit.core.MockAgent]]s
 * which received an invitation send by [[com.github.pheymann.mockit.network.InvitationServer]].
 *
 * One [[com.github.pheymann.mockit.mock.MockUnit]] gets send to exactly one agent with all
 * available components.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class UploadServer(
                    val mockUnits:  List[MockUnitContainer],
                    val components: Map[String, Array[Byte]],
                    val serverIps:  ListBuffer[(String, String, Int)],
                    val shutdown:   ShutdownLatch
                  ) extends ConfigServer
                    with    NetworkClassServer
                    with    Logger
                    with    Runnable {

    val server = new ServerSocket(MOCKIT_UPLOAD_PORT)

    override def run(): Unit = {
        this > s"$start@$MOCKIT_UPLOAD_PORT"
        for {
            mockUnit    <- mockUnits
            mockCounter <- 0 until mockUnit.config.mockNumber
        } {
            try {
                this >> "waiting for client"
                val client  = server.accept
                val address = client.getInetAddress.getHostAddress

                this >> s"[$address][${mockUnit.mockName}}][$mockCounter}] -> send mock unit and component classes"
                sendClasses(mockUnit, components, client)

                this >> s"[$address][${mockUnit.mockName}}][$mockCounter}] -> send configurations"
                sendConfigs(mockUnit, client)

                if (mockUnit.config.mockType.equals(BasicMockType.server)) {
                    this >> s"[$address][${mockUnit.mockName}}][$mockCounter}] -> wait for mock server to go online"
                    serverIps += new Tuple3(mockUnit.mockName, address, mockUnit.config.serverPort)
                    MockFactory.waitUntilOnline(address,mockUnit.config.serverPort)
                }
                client.close()
            }
            catch {
                case e: Throwable => this error ("try to upload mock unit and components", e)
            }
        }
        this >> "all mock units distributed > shutdown invitation server"
        shutdown.close
        this > stop

        server.close()
    }

}
