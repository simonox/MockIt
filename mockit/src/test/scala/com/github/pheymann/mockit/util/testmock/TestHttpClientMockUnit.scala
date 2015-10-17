package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.mock.http.OK
import com.github.pheymann.mockit.mock.http._

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
