package com.github.pheymann.mockit.mock.http

/**
 * Collection of http request methods.
 *
 * @author  pheymann
 * @version 0.1.0
 */
sealed trait HttpMethod {
    val key: String
}

case object Get     extends HttpMethod {
    override val key = "GET"
}
case object Post    extends HttpMethod {
    override val key = "POST"
}
case object Put     extends HttpMethod {
    override val key = "PUT"
}
case object Delete  extends HttpMethod {
    override val key = "DELETE"
}
case object Head    extends HttpMethod {
    override val key = "HEAD"
}
case object Trace   extends HttpMethod {
    override val key = "TRACE"
}
case object Connect extends HttpMethod {
    override val key = "CONNECT"
}
