package org.mockit.mock.udp

import java.net.DatagramPacket

import org.mockit.mock.ProtocolMockUnit

/**
 * Subclass of [[org.mockit.mock.MockUnit]] with specialisations for udp
 * mock units.
 *
 * The attribute ''receivePackage'' is set when a new package
 * is received by the underlying worker.
 *
 * @see [[org.mockit.mock.ProtocolMockUnit.waitOnData()]]
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class UDPMockUnit extends ProtocolMockUnit {

    var receivePackage: DatagramPacket = null

}
