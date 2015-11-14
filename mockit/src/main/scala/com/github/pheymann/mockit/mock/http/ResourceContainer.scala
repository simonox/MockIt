package com.github.pheymann.mockit.mock.http

object ResourceContainer {

    def wrap(
                request:    HttpRequest,
                response:   HttpRequest => HttpResponse,
                error:      Option[HttpRequest => HttpResponse]
            ): Option[ResourceContainer] = {
        Option(ResourceContainer(request, response, error))
    }

}

case class ResourceContainer(
                                request:    HttpRequest,
                                response:   HttpRequest => HttpResponse,
                                error:      Option[HttpRequest => HttpResponse]
                            )
