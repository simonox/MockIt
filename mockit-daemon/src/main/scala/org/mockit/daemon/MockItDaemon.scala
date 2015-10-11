package org.mockit.daemon

import java.io.ObjectInputStream
import java.net.{ServerSocket, SocketTimeoutException}

import org.mockit.core._
import org.mockit.logging.Logger
import org.mockit.network.ServerListener

/**
 * Main procedure to run the daemon.
 *
 * this process runs as daemon in the background and waits
 * for the execution of a [[org.mockit.core.MockAgent]].
 *
 * @author  pheymann
 * @version 0.1.0
 */
object MockItDaemon {

    /**
     * Runs a [[org.mockit.network.ServerListener]] and starts a local
     * network client ([[org.mockit.core.MockAgent]]) with the received
     * [[org.mockit.mock.MockUnit]].
     *
     * @param log
     *            log instance given
     */
    def action(log: Logger): Unit = {
        try {
            val listener = new ServerListener

            val address             = listener.waitForInvitation
            val (config, mockClass) = listener.load(address)

            MockFactory.networkClient(config, mockClass)
        }
        catch {
            case e @ (_: SocketTimeoutException | _: UploadServerDownException) =>
            case e: Throwable =>
                log error ("exception during MockIt test run", e)
                throw e
        }
    }

    /**
     * Client which waits for '''MockIt-Tool''' interactions.
     *
     * The tool starts a daemon with a given id. this id is added
     * to the standard port to produce the port of this daemon.
     *
     * If the tool sends a quit-command the daemon wait for the shutdown
     * of the running [[org.mockit.core.MockAgent]] and sends an
     * acknowledgement in the end.
     *
     * @param args
     *             id of the actual daemon instance
     */
    def main(args: Array[String]): Unit = {
        val log = new Logger {override lazy val name = "MockItDaemon"}

        var continue = true
        var daemon: Thread = null

        val id     = args(0).toInt
        val server = new ServerSocket(MOCKIT_PORT + id)

        server.setSoTimeout(SERVER_TIMEOUT)

        log > s"start MockIt test client := $id"
        do {
            daemon = new Thread {
                override def run(): Unit = action(log)
            }
            daemon.start()

            try {
                val client = server.accept

                val inStream = new ObjectInputStream(client.getInputStream)

                inStream.readObject match {
                    case ShutdownDaemon() => continue = false
                    case _ =>
                }
                client.getOutputStream.write(0)
                client.getOutputStream.flush()
                client.close()
                if (!continue)
                    server.close()
            }
            catch {
                case e: SocketTimeoutException =>
                case e: Throwable => throw e
            }
            daemon.join()
        } while (continue)
        log > s"stop MockIt test client := $id"
    }

}