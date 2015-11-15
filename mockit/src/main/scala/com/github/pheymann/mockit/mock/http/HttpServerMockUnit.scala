package com.github.pheymann.mockit.mock.http

import com.github.pheymann.mockit.mock.MockUnit

import scala.collection.mutable

import ResourceHandler._

/**
 * @author  pheymann
 * @version 0.2.0
 */
object HttpServerMockUnit {

    def find(resource: String, command: HttpMethod)
            (implicit unit: HttpServerMockUnit): Option[ResourceContainer] = {
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
            case Some(container) =>
                if (container.request.equals(request))
                    Option(container.response(request))
                else
                    container.error match {
                        case Some(error) => Option(error(request))
                        case None => None
                    }
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
 * @version 0.2.0
 */
abstract class HttpServerMockUnit extends MockUnit {

    val baseDir = ""

    protected val mock = new mutable.HashMap[String, ResourceHandler]

    /**
     * Global error response if no request/response pair can be found.
     *
     * If no global error response is defined the connection to
     * the client is closed.
     *
     * @param request
     *              without corresponding response
     * @return response
     */
    def errorResponse(request: HttpRequest): HttpResponse

    /**
     * Adds request/response pair to the mock server.
     *
     * If the received request fits the defined one the server transmits the
     * stored response to the client. If it doesn't fit the server goes through
     * the following stages:
     *   - if an error is defined send the error response
     *   - if an global error response is defined send this
     *   - nothing is defined send nothing
     *
     * @param request
     *              defined request
     * @param response
     *              standard response
     * @param error
     *              is send when the received request doesn't fit the defined one
     * @return response
     */
    def add(
                request:    HttpRequest,
                response:   HttpResponse,
                error:      Option[HttpRequest => HttpResponse] = None
           ): Unit = {
        mock.get(request.resource) match {
            case Some(resource) => set(request.method, request -> response, error)(resource)
            case None =>
                implicit val resHandler = new ResourceHandler

                set(request.method, request -> response, error)
                mock += request.resource -> resHandler
        }
    }

    /**
     * Adds request/response pair to the mock server.
     *
     * If the received request fits the defined one the server transmits the
     * stored response to the client. If it doesn't fit the server goes through
     * the following stages:
     *   - if an error is defined send the error response
     *   - if an global error response is defined send this
     *   - nothing is defined send nothing
     *
     * @param request
     *              defined request
     * @param handler
     *              function which creates responses depending on the request
     * @param error
     *              is send when the received request doesn't fit the defined one
     * @return response
     */
    def addWithFunction(
                            request:    HttpRequest,
                            handler:    HttpRequest => HttpResponse,
                            error:      Option[HttpRequest => HttpResponse] = None
                       ): Unit = {
        mock.get(request.resource) match {
            case Some(resource) => setHandler(request.method, request -> handler, error)(resource)
            case None =>
                implicit val resHandler = new ResourceHandler

                setHandler(request.method, request -> handler, error)
                mock += request.resource -> resHandler
        }
    }

}
