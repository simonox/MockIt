package com.github.pheymann.mockit.mock.http

import java.io.BufferedReader

/**
 * @author  pheymann
 * @version 0.1.0
 */
object HttpResponse {

    /* header options used only by responses */
    val contentLanguage = "Content-Language"
    val contentLocation = "Content-Location"
    val contentRange    = "Content-Range"

    val acceptRange     = "Accept-Range"

    val age             = "Age"
    val expires         = "Expires"
    val lastModified    = "Last-Modified"

    val allow           = "Allow"

    /**
     * Implicit function to map a [[com.github.pheymann.mockit.mock.http.HttpCode]] string
     * representation to the actual instance.
     * 
     * @param code
     *              string representation
     * @return actual instance
     */
    implicit def findCommand(code: String): HttpCode = {
        code match {
            case stat if OK.key.contains(stat)          => OK
            case stat if NotFound.key.contains(stat)    => NotFound
        }
    }

    /**
     * Creates a [[com.github.pheymann.mockit.mock.http.HttpResponse]] out of
     * the given data.
     *
     * @param code
     * @param inStream
     *              stream of header options and body content
     * @return actual instance
     */
    def generate(
                    code:           HttpCode,
                    inStream:       BufferedReader
                ): HttpResponse = {
        val response = new HttpResponse(code)

        var headerLine = inStream.readLine

        while (headerLine != null && (headerLine != "\r\n" && headerLine != "")) {
            val keyValue = headerLine.split(": ")

            response.addHeader(keyValue(0), keyValue(1))
            headerLine = inStream.readLine
        }
        response.content = inStream
        response
    }

}

/**
 * Message unit for http responses.
 *
 * @param code
 *
 * @author  pheymann
 * @version 0.1.0
 */
case class HttpResponse (
                            code: HttpCode
                        ) extends HttpBasic[HttpResponse] {

    override def equals(obj: Any): Boolean = {
        if (obj == null || !obj.isInstanceOf[HttpResponse])
            false
        else {
            val response = obj.asInstanceOf[HttpResponse]

            var equal   = false

            equal = response.code.equals(this.code)
            for ((key, value) <- this.header) {
                equal &&= {
                    response.header.get(key) match {
                        case Some(value2)   => value2.equals(value)
                        case None           => false
                    }
                }
            }
            equal
        }
    }

    override def toString: String = {
        val builder = new StringBuilder

        builder ++= "HTTP/1.1 " + code.toString + "\r\n"
        for ((key, value) <- header) {
            builder ++= key + ": " + value + "\r\n"
        }
        builder.mkString
    }

}
