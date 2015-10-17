package com.github.pheymann.mockit.logging

import com.github.pheymann.mockit.logging.NetworkLogger.LogLevel.LogLevel

/**
 * Container for a log message.
 *
 * @param level
 *              [[com.github.pheymann.mockit.logging.NetworkLogger.LogLevel.error]],
 *              [[com.github.pheymann.mockit.logging.NetworkLogger.LogLevel.warn]]
 * @param data
 *              additional log data
 * @param exception
 *              exception option
 * @param msg
 *              log description
 */
class LogEntry (
                val level:      LogLevel,
                val data:       LogData,
                val exception:  Option[Throwable],
                val msg:        String
               ) extends Serializable {

    override def equals(obj: Any): Boolean = {
        var equal = false

        if (obj != null && obj.isInstanceOf[LogEntry]) {
            val entry = obj.asInstanceOf[LogEntry]

            equal = {
                this.level.equals(entry.level) &&
                this.data.equals(entry.data) && {
                    if (this.msg != null)
                        this.msg.equals(entry.msg)
                    else
                        false
                }
            }
        }
        equal
    }

    override def toString: String = {
        "log entry: [%s] [%s] [%s]".format(level, data.machine, msg)
    }

}
