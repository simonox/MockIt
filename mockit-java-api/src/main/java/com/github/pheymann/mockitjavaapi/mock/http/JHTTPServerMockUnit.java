package com.github.pheymann.mockitjavaapi.mock.http;

import java.util.function.Function;

import scala.Option;

import com.github.pheymann.mockit.core.ConvertUtil$;
import com.github.pheymann.mockit.mock.http.HttpRequest;
import com.github.pheymann.mockit.mock.http.HttpResponse;
import com.github.pheymann.mockit.mock.http.HttpServerMockUnit;

/**
 * This subclass is the java interface. It wraps the scala
 * functions including special signs into a java style
 * representation.
 *
 * @see com.github.pheymann.mockit.mock.http.HttpServerMockUnit
 *
 * @author  pheymann
 * @version 0.1.0
 */
public abstract class JHTTPServerMockUnit extends HttpServerMockUnit {

    public abstract void jinit() throws Exception;

    @Override
    public void init() {
        try {
            jinit();
        }
        catch (Throwable e) {}
    }

    /**
     * Adds request/response pair to the mock server.
     *
     * If the received request meets the defined one the server transmits the
     * stored response to the client. If it doesn't meet the server goes through
     * the following stages:
     *   - if an error is defined send the error response
     *   - if an global error response is defined send this
     *   - nothing is defined send nothing
     *
     * @param request
     *              defined request
     * @param response
     *              standard response
     */
    public void jadd(HttpRequest request, HttpResponse response) {
        add(request, response, Option.apply(null));
    }

    /**
     * Adds request/response pair to the mock server.
     *
     * If the received request meets the defined one the server transmits the
     * stored response to the client. If it doesn't meet the server goes through
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
     *              is send when the received request doesn't meet the defined one
     */
    public void jadd(HttpRequest request, HttpResponse response, Function<HttpRequest, HttpResponse> error) {
        add(request, response, Option.apply(ConvertUtil$.MODULE$.javaToScala(error)));
    }

    /**
     * Adds request/response-handler pair to the mock server.
     *
     * If the received request meets the defined one the server calls the response
     * creation function and transmits the retrieved response to the client. If
     * it doesn't meet the server goes through the following stages:
     *   - if an error is defined send the error response
     *   - if an global error response is defined send this
     *   - nothing is defined send nothing
     *
     * @param request
     *              defined request
     * @param handler
     *              function which creates responses depending on the request
     */
    public void jaddWithFunction(
        HttpRequest request,
        Function<HttpRequest, HttpResponse> handler
    ) {
        addWithFunction(request, ConvertUtil$.MODULE$.javaToScala(handler), Option.apply(null));
    }

    /**
     * Adds request/response-handler pair to the mock server.
     *
     * If the received request meets the defined one the server calls the response
     * creation function and transmits the retrieved response to the client. If
     * it doesn't meet the server goes through the following stages:
     *   - if an error is defined send the error response
     *   - if an global error response is defined send this
     *   - nothing is defined send nothing
     *
     * @param request
     *              defined request
     * @param handler
     *              function which creates responses depending on the request
     * @param error
     *              is send when the received request doesn't meet the defined one
     */
    public void jaddWithFunction(
        HttpRequest request,
        Function<HttpRequest, HttpResponse> handler,
        Function<HttpRequest, HttpResponse> error
    ) {
        addWithFunction(
            request,
            ConvertUtil$.MODULE$.javaToScala(handler),
            Option.apply(ConvertUtil$.MODULE$.javaToScala(error))
        );
    }

}
