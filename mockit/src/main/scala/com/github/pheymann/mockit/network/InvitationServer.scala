package com.github.pheymann.mockit.network

import java.net.{DatagramPacket, MulticastSocket, InetAddress}

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core.ShutdownLatch
import com.github.pheymann.mockit.logging.Logger

/**
 * @author  pheymann
 * @version 0.1.0
 */
object InvitationServer {

    /**
     * Creates and starts a ''Threads'' which runs an instance
     * of [[InvitationServer]].
     *
     * @param shutdown
     *              shutdown signal
     * @return actual thread instance
     */
    def init(shutdown: ShutdownLatch): Thread = {
        val worker = new Thread(new InvitationServer(shutdown))

        worker.start()
        worker
    }

}

/**
 * Server instance to send udp invitations to all [[com.github.pheymann.mockit.core.MockAgent]]s
 * available in the network.
 *
 * This server gets shutdown externally by a signal.
 *
 * @param shutdown
 *              shutdown signal
 *
 * @author  pheymann
 * @version 0.1.0
 */
class InvitationServer(
                        val shutdown: ShutdownLatch
                     ) extends Runnable
                       with    Logger {

    val mcAddress = InetAddress.getByName(MOCKIT_MC_ADDRESS)
    val multiCast = new MulticastSocket(MOCKIT_MC_PORT)

    multiCast.setReuseAddress(true)
    multiCast.joinGroup(mcAddress)

    override def run(): Unit = {
        this > s"$start@$MOCKIT_MC_PORT"
        do {
            this >> "invite mock agents"
            val greeting = new DatagramPacket(new Array[Byte](0), 0, 0, mcAddress, MOCKIT_MC_PORT)

            multiCast.send(greeting)
            try {
                multiCast.send(greeting)
            }
            catch {
                case e: Throwable => this error ("cannot send invitation to MockAgent", e)
            }

            try {
                Thread.sleep(SERVER_TIMEOUT)
            }
            catch {
                case e: Throwable => this warn ("server sleep interruption occurred", e)
            }
        } while (shutdown.continue)
        this >> "all mock agents invited"
        this > stop

        multiCast.close()
    }

}
