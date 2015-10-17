package com.github.pheymann.mockit.network

import java.io.ObjectOutputStream
import java.net.Socket

import com.github.pheymann.mockit.core.MockUnitContainer

/**
 * Abstracts the [[com.github.pheymann.mockit.core.Configuration]] treatment for
 * server side.
 *
 * Necessary to keep [[com.github.pheymann.mockit.network.UploadServer]] clean and
 *
 * @author  pheymann
 * @version 0.1.0 *
 */
trait ConfigServer {

    def sendConfigs(mockUnit: MockUnitContainer, client: Socket): Unit = {
        val outStream = new ObjectOutputStream(client.getOutputStream)

        outStream.writeObject(mockUnit.config)
        outStream.flush()
    }

}
