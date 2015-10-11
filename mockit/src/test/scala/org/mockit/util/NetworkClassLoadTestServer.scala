package org.mockit.util

import java.net.ServerSocket

import org.mockit.core._
import org.mockit.mock.MockUnit
import org.mockit.networkclassloader.{ClassConverter, NetworkClassServer}

class NetworkClassLoadTestServer(
                                    val mockClass:  Class[_ <: MockUnit],
                                    val components: Map[String, Class[_]]
                                )   extends NetworkClassServer
                                    with    Runnable {

    import ClassConverter._

    val server = new ServerSocket(MOCKIT_UPLOAD_PORT)

    override def run: Unit = {
        val client = server.accept

        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            null
        )
        sendClasses(container, components, client)

        client.close
        server.close
    }

}
