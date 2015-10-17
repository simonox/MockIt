package com.github.pheymann.mockit.mock.http

/**
 * Collection of http response codes. Not all codes
 * defined by the standard are included by now but
 * can be created via this trait.
 *
 * @author  pheymann
 * @version 0.1.0
 */
trait HttpCode {
    val key: String
}

case object OK              extends HttpCode {
    override val key = "200 OK"
}
case object Created         extends HttpCode {
    override val key = "201 Created"
}
case object Accepted        extends HttpCode {
    override val key = "202 Accepted"
}
case object NoContent       extends HttpCode {
    override val key = "204 NoContent"
}
case object ResetContent    extends HttpCode {
    override val key = "205 ResetContent"
}

case object MovedPerm       extends HttpCode {
    override val key = "301 Moved Permanently"
}
case object Found           extends HttpCode {
    override val key = "302 Found"
}
case object NotModified     extends HttpCode {
    override val key = "304 Not Modified"
}
case object TempRedirected  extends HttpCode {
    override val key = "307 Temporary Redirected"
}

case object BadRequest      extends HttpCode{
    override val key = "400 Bad Request"
}
case object Unauthorized    extends HttpCode{
    override val key = "401 Unauthorized"
}
case object Forbidden       extends HttpCode{
    override val key = "403 Forbidden"
}
case object NotFound        extends HttpCode{
    override val key = "404 Not Found"
}
case object MethNotAllowed  extends HttpCode{
    override val key = "405 Method Not Allowed"
}
case object NotAcceptable   extends HttpCode{
    override val key = "406 Not Acceptable"
}
case object RequestTimeout  extends HttpCode{
    override val key = "408 Request Timeout"
}
case object Conflict        extends HttpCode{
    override val key = "409 Conflict"
}
case object Gone            extends HttpCode{
    override val key = "410 Gone"
}
case object LengthRequired  extends HttpCode{
    override val key = "411 Length Required"
}