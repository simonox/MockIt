package com.github.pheymann.mockit.logging

import com.typesafe.scalalogging.LazyLogging

/**
 * Pattern for local logging based on '''log4j'''.
 *
 * @author  pheymann
 * @version 0.1.0
 */
trait Logger extends LazyLogging {

    val start   = "start"
    val stop    = "stop"

    lazy val name   = this.getClass.getSimpleName


    /**
     * Logging of Info message.
     *
     * @param msg
     *          info message transferred call-by-name
     */
    def >(msg: => String): Unit = {
        logger.info(msg)
    }

    /**
     * Logging of Trace message.
     *
     * @param msg
     *          trace message transferred call-by-name
     */
    def >>(msg: => String): Unit = {
        logger.trace(msg)
    }

    /**
     * Logging of Warn message.
     *
     * @param msg
     *          warn message
     * @param e
     *          optional exception
     */
    def warn(msg: String, e: Throwable = null): Unit = {
        logger.warn(msg, e)
    }

    /**
     * Logging of Error message.
     *
     * @param msg
     *          error message
     * @param e
     *          optional exception
     */
    def error(msg: String, e: Throwable = null): Unit = {
        logger.error(msg, e)
    }

}
