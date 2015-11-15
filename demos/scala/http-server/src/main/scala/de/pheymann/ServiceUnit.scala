package de.pheymann

import com.github.pheymann.mockit.annotation.{MockItConfigs, MockItConfig, MockIt}
import com.github.pheymann.mockit.core.{ConnectionType, ServerConfiguration}
import com.github.pheymann.mockit.mock.http._

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

    override def errorResponse(request: HttpRequest): HttpResponse = {
        HttpResponse(BadRequest)
    }

}