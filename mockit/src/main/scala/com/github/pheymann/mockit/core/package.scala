package com.github.pheymann.mockit.core

import java.util.concurrent.TimeUnit

import com.github.pheymann.mockit.core.ConfigProvider._

/**
 * @author  pheymann
 * @version 0.1.0
 */
package object core {

    //* constants
    final val STUFFING_INT              = -1
    final val STUFFING_STRING           = ""

    final val DEFAULT_PORT              = config("default.port").toInt
    final val DEFAULT_THREAD_NUM        = config("default.thread-number").toInt
    final val DEFAULT_MOCK_NUM          = config("default.mock-number").toInt
    final val DEFAULT_REPETITIONS       = config("default.repetitions").toInt
    final val DEFAULT_IP                = config("default.ip")
    final val DEFAULT_ENC               = "UTF-8"

    final val DEFAULT_SHUTDOWN_TIME     = config("default.shutdown.time").toInt
    final val DEFAULT_SHUTDOWN_UNIT     = TimeUnit.MINUTES

    final val JOIN_WITHOUT_CONSTRAINTS  = 0
    final val SERVER_TIMEOUT            = config("server.timeout").toInt
    final val SERVER_WAIT_REPETITIONS   = config("server.wait-repetitions").toInt

    final val MOCKIT_PORT               = config("server.port.mockit").toInt
    final val MOCKIT_UPLOAD_PORT        = config("server.port.mockit.upload").toInt
    final val MOCKIT_LOG_PORT           = config("server.port.mockit.log").toInt
    final val MOCKIT_MC_PORT            = config("server.port.mockit.mc").toInt
    final val MOCKIT_SD_PORT            = config("server.port.mockit.sd").toInt

    final val MOCKIT_MC_ADDRESS         = config("server.address.mc")

    final val REGISTER_FILE             = config("register-file")

    final val CONFIGS                   = "mockit"


    //* parameters
    var machine: String                 = "no-machine-id"


    case class ShutdownDaemon()

}

//* types
//* enums
object BasicMockType extends Enumeration {
    type BasicMockType = Value

    val client, server, agent, p2p, none = Value
}

object ConnectionType extends Enumeration {
    type ConnectionType = Value

    val udp, tcp, http, none = Value
}