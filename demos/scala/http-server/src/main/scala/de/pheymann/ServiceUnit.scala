package de.pheymann

import org.mockit.annotation.{MockItConfigs, MockItConfig, MockIt}
import org.mockit.core.{ConnectionType, ServerConfiguration}
import org.mockit.mock.http._

@MockIt(mockKeys = Array("test-key"))
@MockItConfigs
class ServiceUnit extends HttpServerMockUnit {

    @MockItConfig(mockUnit = "ServiceUnit", mockKey = "test-key")
    val httpConfig = new ServerConfiguration(
        8080,
        1,
        ConnectionType.http
    )

    override def init: Unit = {
        WebappService(this)
        WebappTestService(this)
    }

}