package com.github.pheymann.mockitjavaapi.core;

import com.github.pheymann.mockit.mock.http.*;

/**
 * Java wrapper for {@link com.github.pheymann.mockit.mock.http.HttpCode}. For a detailed
 * description of the parameter use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JHttpCode {

    /*
     * regex to transform from scala implementation to java:
     *  find:       ^(\s+)(case object )([a-zA-Z]*)(.*)\n(.*)\n(.*)
     *  replace:    $1public static $3\\\$ $3 = $3\\\$.MODULE\\\$;
     */

    public static OK$ OK                            = OK$.MODULE$;
    public static Created$          Created         = Created$.MODULE$;
    public static Accepted$         Accepted        = Accepted$.MODULE$;
    public static NoContent$        NoContent       = NoContent$.MODULE$;
    public static ResetContent$     ResetContent    = ResetContent$.MODULE$;

    public static MovedPerm$        MovedPerm       = MovedPerm$.MODULE$;
    public static Found$            Found           = Found$.MODULE$;
    public static NotModified$      NotModified     = NotModified$.MODULE$;
    public static TempRedirected$   TempRedirected  = TempRedirected$.MODULE$;

    public static BadRequest$       BadRequest      = BadRequest$.MODULE$;
    public static Unauthorized$     Unauthorized    = Unauthorized$.MODULE$;
    public static Forbidden$        Forbidden       = Forbidden$.MODULE$;
    public static NotFound$         NotFound        = NotFound$.MODULE$;
    public static MethNotAllowed$   MethNotAllowed  = MethNotAllowed$.MODULE$;
    public static NotAcceptable$    NotAcceptable   = NotAcceptable$.MODULE$;
    public static RequestTimeout$   RequestTimeout  = RequestTimeout$.MODULE$;
    public static Conflict$         Conflict        = Conflict$.MODULE$;
    public static Gone$             Gone            = Gone$.MODULE$;
    public static LengthRequired$   LengthRequired  = LengthRequired$.MODULE$;

}