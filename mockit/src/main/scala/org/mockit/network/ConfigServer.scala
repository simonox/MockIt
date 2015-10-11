package org.mockit.network

import java.io.ObjectOutputStream
import java.net.Socket

import org.mockit.core.MockUnitContainer

/**
 * Abstracts the [[org.mockit.core.Configuration]] treatment for
 * server side.
 *
 * Necessary to keep [[org.mockit.network.UploadServer]] clean and
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
