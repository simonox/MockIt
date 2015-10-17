package com.github.pheymann.mockit.mock.http

/**
 * @author  pheymann
 * @version 0.1.0
 */
object ResourceHandler {

    def set(command: HttpMethod, body: (HttpRequest, HttpResponse))
           (implicit resource: ResourceHandler): Unit = {
        val (request, response) = body

        setHandler(command, (request, _ => response))
    }

    def setHandler(command: HttpMethod, body: (HttpRequest, HttpRequest => HttpResponse))
                  (implicit resource: ResourceHandler): Unit = {
        val (request, response) = body

        request.method match {
            case Get        => resource.get = Option(request -> response)
            case Post       => resource.post = Option(request -> response)
            case Put        => resource.put = Option(request -> response)
            case Delete     => resource.delete = Option(request -> response)
            case Head       => resource.head = Option(request -> response)
            case Trace      => resource.trace = Option(request -> response)
            case Connect    => resource.connect = Option(request -> response)
        }
    }

    def get(command: HttpMethod)
           (implicit resource: ResourceHandler): Option[(HttpRequest, HttpRequest => HttpResponse)] = {
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

    var get:        Option[(HttpRequest, HttpRequest => HttpResponse)] = None
    var post:       Option[(HttpRequest, HttpRequest => HttpResponse)] = None
    var put:        Option[(HttpRequest, HttpRequest => HttpResponse)] = None
    var delete:     Option[(HttpRequest, HttpRequest => HttpResponse)] = None
    var head:       Option[(HttpRequest, HttpRequest => HttpResponse)] = None
    var trace:      Option[(HttpRequest, HttpRequest => HttpResponse)] = None
    var connect:    Option[(HttpRequest, HttpRequest => HttpResponse)] = None

}
