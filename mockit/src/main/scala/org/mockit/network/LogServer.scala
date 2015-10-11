package org.mockit.network

import java.io.ObjectInputStream
import java.net.{SocketTimeoutException, ServerSocket}

import scala.collection.mutable.ListBuffer

import org.mockit.core._
import org.mockit.logging.{Logger, LogEntry}

/**
 * @author  pheymann
 * @version 0.1.0
 */
object LogServer {

    /**
     * Creates and starts a ''Threads'' which runs an instance
     * of [[LogServer]].
     *
     * @param logs
     *          external reference to a [[org.mockit.logging.LogEntry]] buffer
     * @param agentNumber
     *          number of [[org.mockit.core.MockAgent]]s which are needed
     * @return actual thread instance
     */
    def init(logs: ListBuffer[LogEntry], agentNumber: Int): Thread = {
        val worker = new Thread(new LogServer(logs, agentNumber))

        worker.start()
        worker
    }

}

/**
 * Server instance to collect all [[org.mockit.logging.LogEntry]] from
 * the [[org.mockit.core.MockAgent]]s included in this mock up.
 *
 * Shutdowns down when all log messages from all agents are received.
 *
 * @param logs
 *              external reference to a [[org.mockit.logging.LogEntry]] buffer
 * @param agentNumber
 *              number of [[org.mockit.core.MockAgent]]s which are needed
 *
 * @author  pheymann
 * @version 0.1.0
 */
class LogServer(
                val logs:           ListBuffer[LogEntry],
                val agentNumber:    Int
               ) extends Runnable
                 with    Logger {

    val logServer = new ServerSocket(MOCKIT_LOG_PORT)

    override def run(): Unit = {
        var counter = 0

        logServer.setSoTimeout(SERVER_TIMEOUT)

        this > s"$start@$MOCKIT_LOG_PORT"
        do {
            try {
                this >> "waiting for client"
                val client = logServer.accept
                val address = client.getInetAddress.getHostAddress

                val logStream = new ObjectInputStream(client.getInputStream)
                val size = logStream.readInt

                this >> s"[$address]: collect all $size log entries"
                for (i <- 0 until size)
                    logs += logStream.readObject.asInstanceOf[LogEntry]

                client.close()
                counter += 1
            }
            catch {
                case e: SocketTimeoutException => this >> "server listening interruption occurred"
                case e: Throwable => this error ("disregard of log protocol", e)
            }
        } while (counter < agentNumber)
        this > stop

        logServer.close()
    }

}
