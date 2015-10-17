package com.github.pheymann.mockit.mock.http

import com.github.pheymann.mockit.mock.MockUnit

import scala.collection.mutable

import ResourceHandler._

/**
 * @author  pheymann
 * @version 0.1.0
 */
object HttpServerMockUnit {

    def find(resource: String, command: HttpMethod)
            (implicit unit: HttpServerMockUnit): Option[(HttpRequest, HttpRequest => HttpResponse)] = {
        val resHandlerOpt = unit.mock.get(resource)

        resHandlerOpt match {
            case Some(resHandler) => get(command)(resHandler)
            case None => None
        }
    }

    def find(request: HttpRequest)
            (implicit unit: HttpServerMockUnit): Option[HttpResponse] = {
        val optional = find(request.resource, request.method)

        optional match {
            case Some((requestOrig, response)) =>
                if (requestOrig.equals(request))
                    Option(response(request))
                else
                    None
            case None => None
        }
    }

}

/**
 * Subclass of [[com.github.pheymann.mockit.mock.MockUnit]] which specialisations for http
 * server. Especially it provides an attribute to store a basic directory
 * from which resource files can be loaded automatically on request.
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class HttpServerMockUnit extends MockUnit {

    val baseDir = ""

    protected val mock = new mutable.HashMap[String, ResourceHandler]

    def add(request: HttpRequest, response: HttpResponse): Unit = {
        mock.get(request.resource) match {
            case Some(resource) => set(request.method, request -> response)(resource)
            case None =>
                implicit val resHandler = new ResourceHandler

                set(request.method, request -> response)
                mock += request.resource -> resHandler
        }
    }

    protected def add(request: HttpRequest, handler: HttpRequest => HttpResponse): Unit = {
        mock.get(request.resource) match {
            case Some(resource) => setHandler(request.method, request -> handler)(resource)
            case None =>
                implicit val resHandler = new ResourceHandler

                setHandler(request.method, request -> handler)
                mock += request.resource -> resHandler
        }
    }

}
