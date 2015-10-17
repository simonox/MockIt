package com.github.pheymann.mockit.network

import java.net.{Socket, DatagramPacket, MulticastSocket, InetAddress}

import com.github.pheymann.mockit.logging.Logger

import scala.collection.mutable

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.networkclassloader.{CustomObjectInputStream, NetworkClassLoader}
import com.github.pheymann.mockit.core.{Configuration, UploadServerDownException}

/**
 * Listener to track invitations send by [[com.github.pheymann.mockit.network.InvitationServer]]
 * and to set up a mock environment.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ServerListener extends Logger {

    private val classLoader = new NetworkClassLoader

    /**
     * Blocks process until an invitation arrives. Upon this
     * event the source address (ip) is retrieved an returned.
     *
     * @return source address
     */
    def waitForInvitation: String = {
        this > "initialize invitation protocol"
        val mcAddress   = InetAddress.getByName(MOCKIT_MC_ADDRESS)
        val multiCast   = new MulticastSocket(MOCKIT_MC_PORT)

        multiCast.setReuseAddress(true)
        multiCast.joinGroup(mcAddress)
        multiCast.setSoTimeout(SERVER_TIMEOUT * 2)

        this > "wait for invitation"
        val greeting = new DatagramPacket(new Array[Byte](0), 0, 0)

        multiCast.receive(greeting)
        multiCast.close()

        val address = greeting.getAddress.getCanonicalHostName
        this > s"invited by $address"

        address
    }

    /**
     * Loads all class files and configurations necessary to run
     * a mock unit.
     *
     * @param address
     *              source address
     * @throws com.github.pheymann.mockit.core.UploadServerDownException
     *              thrown if an error occurs during data transmission
     * @return configuration and class of the [[com.github.pheymann.mockit.mock.MockUnit]]
     */
    @throws(classOf[UploadServerDownException])
    def load(address: String): (Configuration, Class[_ <: MockUnit]) = {
        var result: (Configuration, Class[_ <: MockUnit]) = null

        try {
            this > s"load from [$address] and define mock unit and components"
            val connection  = new Socket(address, MOCKIT_UPLOAD_PORT)

            this >> s"load from [$address] mockUnit and component classes"
            val mockClass   = classLoader.load(connection)

            this >> s"load from [$address] configurations"
            val inStream    = new CustomObjectInputStream(classLoader, connection.getInputStream)
            val config      = inStream.readObject.asInstanceOf[Configuration]

            connection.close()

            result = (config, mockClass)
        } catch {
            case e: Throwable =>
                this > s"upload server [$address] is already down: " + {
                    var cause: Throwable = e.getCause

                    val msg = new mutable.StringBuilder(e.getMessage)

                    while (cause != null) {
                        msg ++= cause.getMessage
                        cause = cause.getCause
                    }
                    msg.toString()
                }
                throw new UploadServerDownException
        }
        result
    }

}
