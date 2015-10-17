package com.github.pheymann.mockitjavaapi.mock.http;

import java.util.function.Function;

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

    public void jadd(HttpRequest request, Function<HttpRequest, HttpResponse> handler) {
        add(request, ConvertUtil$.MODULE$.javaToScala(handler));
    }

}
