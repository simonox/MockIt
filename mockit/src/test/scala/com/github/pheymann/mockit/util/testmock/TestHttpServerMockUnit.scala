package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.mock.http.HttpRequest
import com.github.pheymann.mockit.mock.http._

object TestHttpServerMockUnit {

    val headerKey   = "user"
    val headerValue = "test-user"

    val content     = "<strong>hello world</strong>"

}

class TestHttpServerMockUnit extends HttpServerMockUnit {

    import TestHttpServerMockUnit._

    def get(request: HttpRequest): HttpResponse = {
        HttpResponse(OK)    +
        (headerKey, headerValue + 1)     ++
        ("text/html", content.getBytes(DEFAULT_ENC))
    }

    def post(request: HttpRequest): HttpResponse = {
        val contentSize = request.header(HttpBasic.contentLength).toInt
        val content = new Array[Char](contentSize)

        request.content.read(content, 0, contentSize)

        val value = content.mkString.toInt

        HttpResponse(OK)       +
            (headerKey, headerValue + 2)    ++
            ("text/html", (value * 2).toString.getBytes(DEFAULT_ENC))
    }

    def error(request: HttpRequest): HttpResponse = {
        HttpResponse(BadRequest) +
            (headerKey, headerValue + 3) ++
            ("text/plain", 3.toString.getBytes(DEFAULT_ENC))
    }

    override def init(): Unit = {
        var request = HttpRequest(Get, "/test") +
            (headerKey, headerValue)
        addWithFunction(request, get _)

        request = HttpRequest(Post, "/test") +
            (headerKey, headerValue)
        addWithFunction(request, post _)

        request = HttpRequest(Put, "/test") +
            (headerKey, headerValue)
        val response = HttpResponse(OK)    +
            (headerKey, headerValue + 3)
        add(request, response, Option(error _))
    }

    override def errorResponse(request: HttpRequest): HttpResponse = {
        HttpResponse(BadRequest) +
            (headerKey, headerValue + 3) ++
            ("text/plain", 4.toString.getBytes(DEFAULT_ENC))
    }

}
