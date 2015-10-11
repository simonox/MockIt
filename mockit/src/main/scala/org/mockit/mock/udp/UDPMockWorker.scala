package org.mockit.mock.udp

import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import java.util.concurrent.Callable

import org.mockit.core._
import org.mockit.logging.{LogChannel, Logger, NetworkLogger}

/**
 * Mock worker instance for [[org.mockit.mock.udp.UDPMockUnit]].
 *
 * Sends or receives packages defined by the protocol of
 * [[org.mockit.mock.udp.UDPMockUnit]]. To receive packages
 * a server port needs to be set and the basic type is
 * [[org.mockit.core.BasicMockType.server]].
 *
 * If a failure is set the response/behaviour gets modified.
 *
 * @param config
 * @param mock
 *
 * @author  pheymann
 * @version 0.1.0
 */
class UDPMockWorker (
                        val config: Configuration,
                        val mock:   UDPMockUnit
                    )   extends  Callable[LogChannel]
                        with     NetworkLogger
                        with     Logger {

    override val mockName = mock.name
    override val mockType = config.mockType.toString

    val udpSocket = {
        config.mockType match {
            case BasicMockType.server | BasicMockType.p2p => new DatagramSocket(config.serverPort)
            case _ => new DatagramSocket
        }
    }

    override def call: LogChannel = {
        val address = InetAddress.getByName(config.targetIp)

        this > s"$start@${config.serverPort}"
        try {
            mock.init()

            while (mock.isNext) {
                if (mock.waitOnData) {
                    this >> "receive package"
                    udpSocket.receive(mock.receivePackage)
                }

                val (response, failure) = mock.mock

                if (response != null || response.length > 0) {
                    this >> s"call mock: response size := ${response.length}} ; failure := ${failure.toString}"

                    val sendPackage = new DatagramPacket(
                        response,
                        0,
                        response.length,
                        address,
                        config.targetPort
                    )
                    failure match {
                        case NoFault()                  => udpSocket.send(sendPackage)
                        case FixedDelay(delay)          =>
                            Thread.sleep(delay)
                            udpSocket.send(sendPackage)
                        case MultipleResponses(factor)  =>
                            for (i <- 0 until factor) {
                                udpSocket.send(sendPackage)
                            }
                        case LooseResponse()            => this >> "don't send response to client"
                        case LooseConnection()          => this >> "don't send response to client"
                    }
                }
                else {
                    this >> "call mock: no response"
                }
            }
        }
        catch {
            case e: Exception =>
                this error (null, e)
                sendError(e = e)
        }
        finally {
            this > stop
            udpSocket.close()
        }
        channel
    }
    
}
