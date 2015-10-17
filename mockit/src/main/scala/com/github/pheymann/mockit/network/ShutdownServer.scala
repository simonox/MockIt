package com.github.pheymann.mockit.network

import java.io.ObjectOutputStream
import java.net.{SocketTimeoutException, ServerSocket}

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.logging.Logger

/**
 * @author  pheymann
 * @version 0.1.0
 */
object ShutdownServer {

    /**
     * Creates and starts a ''Threads'' which runs an instance
     * of [[ShutdownServer]].
     *
     * @param shutdown
     *              shutdown signal
     * @return actual thread instance
     */
    def init(shutdown: ShutdownLatch): Thread = {
        val worker = new Thread(new ShutdownServer(shutdown))

        worker.start()
        worker
    }

}

/**
 * Server instance to act a ''heart beat'' target. Sends shutdown signal
 * if the mock up shall be closed.
 *
 * If this services is not reachable the [[com.github.pheymann.mockit.core.MockAgent]]s
 * shutdown automatically.
 *
 * This server gets shutdown externally by a signal.
 *
 * @param shutdown
 *              shutdown signal
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ShutdownServer(
                        val shutdown: ShutdownLatch
                    )   extends Runnable
                        with    Logger {

    val server = new ServerSocket(MOCKIT_SD_PORT)

    override def run(): Unit = {
        server.setSoTimeout(SERVER_TIMEOUT)

        this > s"$start@$MOCKIT_SD_PORT"
        do {
            try {
                this >> "waiting for clients"
                val client      = server.accept
                val outStream   = new ObjectOutputStream(client.getOutputStream)

                this >> "send 'keep alive' message"
                outStream.writeBoolean(true)
                outStream.flush()

                client.close()
            }
            catch {
                case e: SocketTimeoutException  => this >> "server listening interruption occurred"
                case e: Throwable               => this error (null, e)
            }
        } while (shutdown.continue)
        this > stop

        server.close()
    }

}
