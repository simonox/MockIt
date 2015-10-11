package org.mockit.mock.http

import java.io.{File, BufferedReader}
import java.nio.file.{Paths, Files}

import org.mockit.mock.http.HttpBasic._
import org.mockit.core._

/**
 * Collection of http header options shared by
 * requests and responses.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object HttpBasic {

    val contentType     = "Content-Type"
    val contentLength   = "Content-Length"
    val contentMD5      = "Content-MD5"

    val authorization   = "Authorization"

    val cacheControl    = "Cache-Control"

    val host            = "Host"
    val connection      = "Connection"

    val date            = "Date"

}

/**
 * Basic http communication message. Provides functions
 * to build an modify a request/response.
 *
 * @tparam T
 *          determine the message type:
 *              - [[org.mockit.mock.http.HttpRequest]]
 *              - [[org.mockit.mock.http.HttpResponse]]
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class HttpBasic[T <: HttpBasic[T]] {

    val header  = new collection.mutable.HashMap[String, String]

    var content: BufferedReader = null

    private var body                    = new Array[Byte](0)
    private var bodyFile: File          = null
    private var bodyEncoding: String    = DEFAULT_ENC

    def addHeader(key: String, value: String): Unit = header += key -> value

    /**
     * Adds the body to the message and sets header options
     * ''Content-Type'' and ''Content-Length''
     *
     * @param contType
     *              type of the message, e.g. text/html, text/plain, json, ...
     * @param body
     *              body of the message (content)
     */
    def addBody(contType: String, body: Array[Byte]): Unit = {
        this.body = body

        header += contentType   -> contType
        header += contentLength -> body.length.toString
    }

    /**
     * Adds file reference to the message and sets header option
     * ''Content-Type''. The body will be loaded at runtime.
     *
     * @param contType
     *              type of the message, e.g. text/html, text/plain, json, ...
     * @param file
     *              reference to the content of the message (body)
     * @param encoding
     *              encoding of the file content, e.g. ASCII, UTF-8, ...
     */
    def addBody(contType: String, file: File, encoding: String): Unit = {
        this.bodyFile       = file
        this.bodyEncoding   = encoding

        header += contentType   -> contType
    }

    def +(key: String, value: String): T = {
        addHeader(key, value)
        this.asInstanceOf[T]
    }

    /**
     * @see     [[HttpBasic.addBody()]]
     * @return  reference to this instance
     */
    def ++(contType: String, body: Array[Byte]): T = {
        addBody(contType, body)
        this.asInstanceOf[T]
    }

    /**
     * @see     [[HttpBasic.addBody()]]
     * @return  reference to this instance
     */
    def ++(contType: String, file: File, encoding: String = DEFAULT_ENC): T = {
        addBody(contType, file, encoding)
        this.asInstanceOf[T]
    }

    /**
     * Returns the body content if set explicit, or loads it
     * from an external resource.
     *
     * @return body
     */
    def loadBody: Option[Array[Byte]] = {
        var result: Option[Array[Byte]] = None

        if (body.nonEmpty)
            result = Option(body)
        else if (bodyFile != null) {
            result = Option(Files.readAllBytes(Paths.get(bodyFile.getPath)))
            header += contentLength -> result.get.length.toString
        }
        result
    }

}
