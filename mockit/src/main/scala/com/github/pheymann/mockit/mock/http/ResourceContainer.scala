package com.github.pheymann.mockit.mock.http

/**
 * @author  pheymann
 * @version 0.2.0
 */
object ResourceContainer {

    /**
     * Wraps the parameter into a [[ResourceContainer]]-Option.
     *
     * @param request
 *          Mock awaits a request similar to this one.
     * @param response
 *          Standard response to the request.
     * @param error
 *          If the received request doesn't meet the awaited request,
     *          the error response is sent.
     * @return container-option
     */
    def wrap(
                request:    HttpRequest,
                response:   HttpRequest => HttpResponse,
                error:      Option[HttpRequest => HttpResponse]
            ): Option[ResourceContainer] = {
        Option(ResourceContainer(request, response, error))
    }

}

/**
 * Container storing all data and information relevant to a
 * request.
 *
 * @author  pheymann
 * @version 0.2.0
 *
 * @param request
 *          Mock awaits a request similar to this one.
 * @param response
 *          Standard response to the request.
 * @param error
 *          If the received request doesn't meet the awaited request,
 *          the error response is sent.
 */
case class ResourceContainer(
                                request:    HttpRequest,
                                response:   HttpRequest => HttpResponse,
                                error:      Option[HttpRequest => HttpResponse]
                            )
