package com.github.pheymann.mockit.core

import java.net.ServerSocket
import com.github.pheymann.mockit.mock.MockUnit

abstract class MockServer (
                              override val config:     Configuration,
                              override val mockClass:  Class[_ <: MockUnit]
                          ) extends BasicMockServer(config, mockClass) {

    val server      = new ServerSocket(config.serverPort)
    val serverType  = "standard"
    
}
