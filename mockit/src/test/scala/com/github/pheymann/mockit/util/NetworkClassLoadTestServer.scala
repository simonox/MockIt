package com.github.pheymann.mockit.util

import java.net.ServerSocket

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.networkclassloader.{ClassConverter, NetworkClassServer}

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
