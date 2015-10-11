package org.mockit.mock.http

/**
 * This subclass is the java interface. It wraps the scala
 * functions including special signs into a java style
 * representation.
 *
 * @see [[HttpServerMockUnit]]
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class HttpServerMockUnitConvert extends HttpServerMockUnit {

    import org.mockit.core.ConvertUtil._

    override def init(): Unit = jInit()

    @throws[Exception]
    def jInit(): Unit

    def jAdd(request: HttpRequest, response: HttpResponse): Unit = {
        add(request, response)
    }

    def jAdd(request: HttpRequest, handler: java.util.function.Function[HttpRequest, HttpResponse]): Unit = {
        add(request, handler)
    }

}
