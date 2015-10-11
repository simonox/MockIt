package org.mockit.networkclassloader

import java.io.DataInputStream
import java.net.Socket

/**
 * Collection of functions to read class definitions
 * out of a socket stream.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object ClassInputStream {

    /**
     * Read number of component classes which will be
     * send.
     *
     * @param client
     *          connection to the [[org.mockit.network.UploadServer]]
     * @return number of component classes
     */
    def componentNumber(client: Socket): Int = {
        val input = new DataInputStream(client.getInputStream)

        input.readInt
    }

    /**
     * Receives one class definition and his canonical name.
     *
     * @param client
     *          connection to the [[org.mockit.network.UploadServer]]
     * @return name and definition of a component class
     */
    def receive(client: Socket): (String, Array[Byte]) = {
        val input = new DataInputStream(client.getInputStream)

        var length: Int     = input.readInt
        var name:   String  = null
        var result: Array[Byte] = null

        if (length > 0) {
            val bytes = new Array[Byte](length)

            input.readFully(bytes, 0, bytes.length)
            name = new String(bytes)
        }

        length = input.readInt
        if (length > 0) {
            result = new Array[Byte](length)

            input.readFully(result, 0, result.length)
        }
        (name, result)
    }

}
