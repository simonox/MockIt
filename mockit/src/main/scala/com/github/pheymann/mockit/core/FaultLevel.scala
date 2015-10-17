package com.github.pheymann.mockit.core

/**
 * Fault level which can be used to simulated a
 * faulty behaviour of a distributed system.
 *
 * @author  pheymann
 * @version 0.1.0
 */
sealed trait FaultLevel {
    val factor: Int
    val time:   Long
}

/**
 * The system behaves normal (without failures).
 */
case class NoFault() extends FaultLevel {
    val factor: Int = 0
    val time:   Long = 0
}

/**
 * The system sends responses with fixed delays.
 *
 * @param time
 *          delay time
 */
case class FixedDelay (val time: Long) extends FaultLevel {
    val factor: Int = 0
}

/**
 * The system looses responses because of network failures.
 */
case class LooseResponse() extends FaultLevel {
    val factor: Int = 0
    val time:   Long = 0
}

/**
 * The system looses the connection because of machine,
 * process or application failure or network changes.
 */
case class LooseConnection() extends FaultLevel {
    val factor: Int = 0
    val time:   Long = 0
}

/**
 * The system send responses multiple times (replication).
 *
 * @param factor
 *              replication factor
 */
case class MultipleResponses(val factor: Int) extends FaultLevel {
    val time:   Long = 0
}
