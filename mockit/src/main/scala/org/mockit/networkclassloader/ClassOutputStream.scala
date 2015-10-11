package org.mockit.networkclassloader

import java.io._
import java.net.Socket

import org.mockit.logging.Logger

/**
 * Collection of functions to send class
 * definitions into a socket stream.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object ClassOutputStream {

    /**
     * Sends the number of component classes and the
     * class definitions with canonical name to the
     * target client.
     *
     * @param components
     *              canonical name and class definition as byte array
     * @param client
     *              connection to the target [[org.mockit.network.ServerListener]]
     * @param log
     *              log buffer
     */
    def send(components: Map[String, Array[Byte]], client: Socket, log: Logger): Unit = {
        val output = new DataOutputStream(client.getOutputStream)

        output.writeInt(components.size)
        output.flush()

        for ((name, classBytes) <- components) {
            log >> s"send component class $name"
            ClassOutputStream.send(classBytes, name, client)
        }
    }

    /**
     * Sends a class definition and the canonical name to a
     * target client.
     *
     * @param bytes
     *              class definition
     * @param name
     *              canonical name
     * @param client
     *              connection to the target [[org.mockit.network.ServerListener]]
     */
    def send(bytes: Array[Byte], name: String, client: Socket): Unit = {
        val output = new DataOutputStream(client.getOutputStream)

        output.writeInt(name.length)
        output.write(name.getBytes("UTF-8"))
        output.flush()

        output.writeInt(bytes.length)
        output.write(bytes)
        output.flush()
    }

}
