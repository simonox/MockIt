package de.pheymann

import java.io.File

import com.github.pheymann.mockit.mock.http._

case class WebappTestService(
                                unit: HttpServerMockUnit
                            ) {

    unit add(
            HttpRequest(Get, "/webapp/test"),
            HttpResponse(OK) ++
                ("text/html", new File("html/test.html"), "UTF-8")
        )


    unit add(
            HttpRequest(Post, "/webapp/test") +
                ("username", "pheymann"),
            HttpResponse(OK) ++
                ("text/plain", "successfully uploaded 2".getBytes("UTF-8"))
        )

}