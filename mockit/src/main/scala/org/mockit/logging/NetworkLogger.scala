package org.mockit.logging

import java.io.{IOException, ObjectOutputStream}
import java.net.{Socket, UnknownHostException}

import org.mockit.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
object NetworkLogger {

    object LogLevel extends Enumeration {
        type LogLevel = Value

        val warn, error = Value
    }

    /**
     * Sends all [[org.mockit.logging.LogEntry]] of a
     * [[org.mockit.logging.LogChannel]] to the destination
     * [[org.mockit.network.LogServer]].
     *
     * @param origin
     *              ip of the destination [[org.mockit.network.LogServer]]
     * @param logs
     * @throws java.net.UnknownHostException
     *              if the ip does not target a valid server
     * @throws java.io.IOException
     *              if an error occurs during transmission
     */
    @throws(classOf[UnknownHostException])
    @throws(classOf[IOException])
    def sendLogsAndClear(origin: String, logs: LogChannel): Unit = {
        val socket = new Socket(origin, MOCKIT_LOG_PORT)

        val output = new ObjectOutputStream(socket.getOutputStream)

        logs send output
        logs.clear()

        socket.close()
    }

}

/**
 * Pattern for network logging.
 *
 * @author  pheymann
 * @version 0.1.0
 */
trait NetworkLogger {

    import NetworkLogger._

    val channel = new  LogChannel

    protected val mockName: String
    protected val mockType: String

    /**
     * Stores a warn message into the [[org.mockit.logging.LogChannel]].
     *
     * @param msg
     *          warn message
     * @param e
     *          optional exception
     */
    def sendWarn(msg: String, e: Throwable = null): Unit = {
        channel.log(
            LogLevel.warn,
            LogData.create(
                basic   = mockType,
                mock    = mockName),
            Option(e),
            msg
        )
    }

    /**
     * Stores an error message into the [[org.mockit.logging.LogChannel]].
     *
     * @param msg
     *          error message
     * @param e
     *          optional exception
     */
    def sendError(msg: String = null, e: Throwable = null): Unit = {
        channel.log(
            LogLevel.error,
            LogData.create(
                basic   = mockType,
                mock    = mockName),
            Option(e),
            msg
        )
    }

}
