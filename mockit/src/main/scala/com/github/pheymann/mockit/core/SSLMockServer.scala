package com.github.pheymann.mockit.core

import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.{KeyManagerFactory, SSLServerSocket, SSLContext}

import com.github.pheymann.mockit.mock.MockUnit

object SSLMockServer {

    final val KEY_STORE     = "JKS"
    final val KEY_MANAGER   = "SunX509"
    final val TRANSPORT     = "TLS"

}

/**
 * @author  pheymann
 * @version 0.3.0
 */
abstract class SSLMockServer (
                                override val config:     Configuration,
                                override val mockClass:  Class[_ <: MockUnit]
                             )  extends BasicMockServer(config, mockClass) {

    import SSLMockServer._

    val keyStore = KeyStore.getInstance(KEY_STORE)
    keyStore.load(new FileInputStream(config.keyStore), config.password.toCharArray)

    val keyFactory = KeyManagerFactory.getInstance(KEY_MANAGER)
    keyFactory.init(keyStore, config.certificate.toCharArray)

    val context = SSLContext.getInstance(TRANSPORT)
    context.init(keyFactory.getKeyManagers, null, null)

    val factory = context.getServerSocketFactory

    override val server     = factory.createServerSocket(config.serverPort).asInstanceOf[SSLServerSocket]
    override val serverType = "SSL"

}
