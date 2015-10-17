package com.github.pheymann.mockitjavaapi.core;

import com.github.pheymann.mockit.mock.http.*;

/**
 * Java wrapper for {@link com.github.pheymann.mockit.mock.http.HttpMethod}. For a detailed
 * description of the parameter use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JHttpMethod {

    public static final Get$ GET                = Get$.MODULE$;
    public static final Post$       POST        = Post$.MODULE$;
    public static final Put$        PUT         = Put$.MODULE$;
    public static final Delete$     DELETE      = Delete$.MODULE$;
    public static final Head$       HEAD        = Head$.MODULE$;
    public static final Trace$      TRACE       = Trace$.MODULE$;
    public static final Connect$    CONNECT     = Connect$.MODULE$;

}
