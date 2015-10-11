package org.mockit.mock.http

import java.io.BufferedReader

/**
 * @author  pheymann
 * @version 0.1.0
 */
object HttpRequest {

    /* header options used only by requests */
    val accept          = "Accept"
    val acceptCharset   = accept + "-Charset"
    val acceptEncoding  = accept + "-Encoding"
    val acceptLanguage  = accept + "-Language"

    /**
     * Implicit function to map string representations of
     * [[org.mockit.mock.http.HttpMethod]] to the actual
     * instance.
     *
     * @param method
     *              string representation
     * @return actual instance
     */
    implicit def findCommand(method: String): HttpMethod = {
        method match {
            case Get.key        => Get
            case Post.key       => Post
            case Put.key        => Put
            case Delete.key     => Delete
            case Head.key       => Head
            case Trace.key      => Trace
            case Connect.key    => Connect
        }
    }

    /**
     * Creates a [[org.mockit.mock.http.HttpRequest]] out of
     * the given data.
     *
     * @param method
     * @param resource
     * @param inStream
     *              stream of header options and body content
     * @return actual instance
     */
    def generate(
                    method:         HttpMethod,
                    resource:       String,
                    inStream:       BufferedReader
                ): HttpRequest = {
        val request = new HttpRequest(method, resource)

        var headerLine = inStream.readLine

        while (headerLine != null && (headerLine != "\r\n" && headerLine != "")) {
            val keyValue = headerLine.split(": ")

            request.addHeader(keyValue(0), keyValue(1))
            headerLine = inStream.readLine
        }
        request.content = inStream
        request
    }

}

/**
 * Message unit for http requests.
 *
 * @param method
 * @param resource
 *              can be a service or a resource file
 *
 * @author  pheymann
 * @version 0.1.0
 */
case class HttpRequest(
                        method:     HttpMethod,
                        resource:   String
                      ) extends HttpBasic[HttpRequest] {

    override def equals(obj: Any): Boolean = {
        if (obj == null || !obj.isInstanceOf[HttpRequest])
            false
        else {
            val request = obj.asInstanceOf[HttpRequest]

            var equal   = false

            equal = {
                request.method.equals(this.method) &&
                request.resource.equals(this.resource)
            }
            for ((key, value) <- this.header) {
                equal &&= {
                    request.header.get(key) match {
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

        builder ++= method.toString + " " + resource + " HTTP/1.1\r\n"
        for ((key, value) <- header) {
            builder ++= key + ": " + value + "\r\n"
        }
        builder.mkString
    }

}
