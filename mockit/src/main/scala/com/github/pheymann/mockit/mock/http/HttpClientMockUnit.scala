package com.github.pheymann.mockit.mock.http

import scala.collection.mutable

import com.github.pheymann.mockit.mock.MockUnit

/**
 * Subclass of [[com.github.pheymann.mockit.mock.MockUnit]] with specialisations for http
 * clients.
 *
 * @author  pheymann
 * qversion 0.1.0
 */
abstract class HttpClientMockUnit extends MockUnit {

    val mock = new mutable.ListBuffer[(HttpRequest, HttpResponse)]

    def add(request: HttpRequest, response: HttpResponse): Unit = {
        mock += (request -> response)
    }

}
