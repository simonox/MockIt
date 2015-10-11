package org.mockit.util.testmock

import org.mockit.mock.http._

object TestHttpClientMockUnit {

    val headerKey   = "user"
    val headerValue = "test-user"

    val content     = "hello world"

}

class TestHttpClientMockUnit extends HttpClientMockUnit {

    import TestHttpClientMockUnit._

    override def init: Unit = {
        val request = HttpRequest(Get, "/test") +
            (headerKey, headerValue)
        val response = HttpResponse(OK) +
            (headerKey, headerValue)
        add(request, response)
    }

}
