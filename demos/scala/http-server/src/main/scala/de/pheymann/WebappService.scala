package de.pheymann

import java.io.File

import com.github.pheymann.mockit.mock.http._

case class WebappService(
                            unit: HttpServerMockUnit
                        ) {

    unit add(
            HttpRequest(Get, "/webapp") +
                ("username", "pheymann"),
            HttpResponse(OK) ++
                ("text/html", new File("html/webapp.html"), "UTF-8")
        )


    unit add(
            HttpRequest(Post, "/webapp") +
                ("username", "pheymann"),
            HttpResponse(OK) ++
                ("text/plain", "successfully uploaded".getBytes("UTF-8"))
        )

}