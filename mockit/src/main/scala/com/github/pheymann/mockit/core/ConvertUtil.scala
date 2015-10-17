package com.github.pheymann.mockit.core

import com.github.pheymann.mockit.mock.http.{HttpResponse, HttpRequest}
import com.github.pheymann.mockit.mock.http.{HttpRequest, HttpResponse}

/**
 * Collection of implicit functions for java to scala conversion.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object ConvertUtil {

    import scala.collection.JavaConverters._

    implicit def javaToScala(handler: java.util.function.Function[HttpRequest, HttpResponse])
    : HttpRequest => HttpResponse = {
        request => handler.apply(request)
    }

    implicit def javaToScala(container: java.util.List[MockUnitContainer]): List[MockUnitContainer] = {
        container.asScala.toList
    }

    implicit def javaToScala(components: java.util.Map[String, Array[Byte]]): Map[String, Array[Byte]] = {
        components.asScala.toMap
    }

}
