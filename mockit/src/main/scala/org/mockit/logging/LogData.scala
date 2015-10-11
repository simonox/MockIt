package org.mockit.logging

import org.mockit.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
object LogData {

    def create(duration: Long = -1, basic: String = null, mock: String = null): LogData = {
        new LogData(duration = duration, basic = basic, mock = mock)
    }

}

/**
 * Container for additional log information.
 *
 * @param timestamp
 *              current timestamp if not set explicitly
 * @param duration
 *              duration time, -1 if not set explicitly
 * @param machine
 *              machine identifier
 * @param thread
 *              thread identifier (thread name)
 * @param basic
 *              [[org.mockit.core.BasicMockType]] as ''String''
 * @param mock
 *              mock identifier
 *
 * @author  pheymann
 * @version 0.1.0
 */
class LogData(
              val timestamp:    Long    = System.currentTimeMillis,
              val duration:     Long    = -1,

              val machine:      String  = machine,
              val thread:       String  = Thread.currentThread.getName,
              val basic:        String  = null,
              val mock:         String  = null
            ) extends Serializable {

    override def equals(obj: Any): Boolean = {
        var equal = true

        if (obj == null || !obj.isInstanceOf[LogData])
            equal = false
        else {
            val entry = obj.asInstanceOf[LogData]

            equal = {
                timestamp.equals(entry.timestamp) &&
                duration.equals(entry.duration) &&
                machine.equals(entry.machine) &&
                thread.equals(entry.thread)
            }
        }
        equal
    }

}
