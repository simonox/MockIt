package com.github.pheymann.mockit.mock

import com.github.pheymann.mockit.core.FaultLevel

/**
 * Specialization of the basic mock pattern for
 * the implementations of protocols based on tcp and udp.
 *
 * @author  pheymann
 * @version 0.1.0
 */
trait ProtocolMockUnit extends MockUnit {

    /**
     * Determines if a next request/response execution is
     * available. If ''false'' the underlying worker
     * terminates.
     *
     * @return true if execution shall continue, else false
     */
    def isNext: Boolean
    /**
     * Determines if an external message is available for this
     * executions round. If the wrong the underlying worker may
     * get stuck.
     *
     * @return true if message/data is available, else false
     */
    def waitOnData: Boolean

    /**
     * Implements the actual user protocol.
     *
     * @return an array with the result data which shall be send
     *         to the destination and a fault level if necessary.
     */
    def mock: (Array[Byte], FaultLevel)

}
