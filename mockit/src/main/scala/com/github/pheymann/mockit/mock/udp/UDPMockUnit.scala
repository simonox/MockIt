package com.github.pheymann.mockit.mock.udp

import java.net.DatagramPacket

import com.github.pheymann.mockit.mock.ProtocolMockUnit
import com.github.pheymann.mockit.mock.ProtocolMockUnit

/**
 * Subclass of [[com.github.pheymann.mockit.mock.MockUnit]] with specialisations for udp
 * mock units.
 *
 * The attribute ''receivePackage'' is set when a new package
 * is received by the underlying worker.
 *
 * @see [[com.github.pheymann.mockit.mock.ProtocolMockUnit.waitOnData()]]
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class UDPMockUnit extends ProtocolMockUnit {

    var receivePackage: DatagramPacket = null

}
