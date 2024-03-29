package com.github.pheymann.mockit.logging

import java.io.ObjectOutputStream

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.logging.NetworkLogger.LogLevel._

/**
 * Buffer for [[com.github.pheymann.mockit.logging.LogEntry]] which is not thread
 * safe.
 *
 * Stores all [[com.github.pheymann.mockit.logging.LogEntry]] from one mock worker.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class LogChannel {

    private val log = new ListBuffer[LogEntry]

    /**
     * Creates and inserts a [[com.github.pheymann.mockit.logging.LogEntry]] into the
     * channel.
     *
     * @param level
     *              [[com.github.pheymann.mockit.logging.NetworkLogger.LogLevel.error]],
     *              [[com.github.pheymann.mockit.logging.NetworkLogger.LogLevel.warn]]
     * @param data
     *              additional log data
     * @param e
     *              exception option
     * @param msg
     *              log description
     */
    def log(level: LogLevel, data: LogData, e: Option[Throwable], msg: String): Unit = {
        log += new LogEntry(level, data, e, msg)
    }

    /**
     * Adds a channel to this instance.
     *
     * @param log
     *            additional log channel
     */
    def ++=(log: LogChannel): Unit = {
        this.log ++= log.log
    }

    /**
     * Adds a ''ListBuffer'' as channel to this
     * instance.
     *
     * @param logs
     *              list of additional [[com.github.pheymann.mockit.logging.LogEntry]]
     */
    def ++=(logs: ListBuffer[LogChannel]): Unit = {
        logs.foreach(log => ++=(log))
    }

    /**
     * Prints all [[com.github.pheymann.mockit.logging.LogEntry]] to the console.
     */
    def print(): Unit = {
        log.foreach(log => {
            println(log.msg)
            log.exception match {
                case Some(excep) => excep.printStackTrace()
                case None =>
            }
        })
    }

    /**
     * Writes all [[com.github.pheymann.mockit.logging.LogEntry]] into the
     * output stream.
     *
     * @param output
     *              stream to the server destination
     */
    def send(output: ObjectOutputStream): Unit = {
        output.writeInt(log.size)
        output.flush()

        log.foreach(entry => {
            try {
                output.writeObject(entry)
            }
            catch {
                case e: Exception => /* logging is not useful here */
            }
        })
    }

    def clear(): Unit = log.clear()

    def size: Int = log.size

}
