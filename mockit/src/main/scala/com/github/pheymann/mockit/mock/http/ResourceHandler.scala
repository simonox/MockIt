package com.github.pheymann.mockit.mock.http

import ResourceContainer._

/**
 * @author  pheymann
 * @version 0.1.0
 */
object ResourceHandler {

    def set(
                command:    HttpMethod,
                body:       (HttpRequest, HttpResponse),
                error:      Option[HttpRequest => HttpResponse]
           )
           (implicit resource: ResourceHandler): Unit = {
        val (request, response) = body

        setHandler(command, (request, _ => response), error)
    }

    def setHandler(
                    command:    HttpMethod,
                    body:       (HttpRequest, HttpRequest => HttpResponse),
                    error:      Option[HttpRequest => HttpResponse]
                  )
                  (implicit resource: ResourceHandler): Unit = {
        val (request, response) = body

        request.method match {
            case Get        => resource.get     = wrap(request, response, error)
            case Post       => resource.post    = wrap(request, response, error)
            case Put        => resource.put     = wrap(request, response, error)
            case Delete     => resource.delete  = wrap(request, response, error)
            case Head       => resource.head    = wrap(request, response, error)
            case Trace      => resource.trace   = wrap(request, response, error)
            case Connect    => resource.connect = wrap(request, response, error)
        }
    }

    def get(command: HttpMethod)
           (implicit resource: ResourceHandler): Option[ResourceContainer] = {
        command match {
            case Get        => resource.get
            case Post       => resource.post
            case Put        => resource.put
            case Delete     => resource.delete
            case Head       => resource.head
            case Trace      => resource.trace
            case Connect    => resource.connect
        }
    }

}

/**
 * Container which maps [[HttpMethod]]s to the
 * corresponding [[com.github.pheymann.mockit.mock.http.HttpResponse]]s.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ResourceHandler {

    var get:        Option[ResourceContainer] = None
    var post:       Option[ResourceContainer] = None
    var put:        Option[ResourceContainer] = None
    var delete:     Option[ResourceContainer] = None
    var head:       Option[ResourceContainer] = None
    var trace:      Option[ResourceContainer] = None
    var connect:    Option[ResourceContainer] = None

}
