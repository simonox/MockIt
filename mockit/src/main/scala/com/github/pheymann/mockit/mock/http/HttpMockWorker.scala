package com.github.pheymann.mockit.mock.http

import java.io.{DataOutputStream, InputStreamReader, BufferedReader}
import java.net.Socket
import java.nio.file.{Paths, Files}
import java.util.concurrent.Callable

import com.github.pheymann.mockit.core.BasicMockType
import com.github.pheymann.mockit.logging.{NetworkLogger, Logger, LogChannel}

/**
 * Mock worker instance for [[com.github.pheymann.mockit.mock.http.HttpServerMockUnit]].
 *
 * Is called if a http clients requests a connection to the actual
 * server and handles the incoming request.
 *
 * @param connection
 *              to the http client
 * @param mock
 *              mock unit
 *
 * @author  pheymann
 * @version 0.1.0
 */
class HttpMockWorker (
                         val connection:         Socket,
                         val mock:               HttpServerMockUnit
                     )   extends  Callable[LogChannel]
                                  with     NetworkLogger
                                  with     Logger {

    import HttpRequest._

    override val mockName = mock.name
    override val mockType = BasicMockType.server.toString

    override def call: LogChannel = {
        this > s"$start@[${connection.getInetAddress.getHostAddress}]"

        try {
            mock.init()

            val inStream = new BufferedReader(new InputStreamReader(connection.getInputStream))
            val outStream = new DataOutputStream(connection.getOutputStream)

            val primaries = inStream.readLine.split(" ")

            val request = HttpRequest.generate(primaries(0), primaries(1), inStream)

            val response = HttpServerMockUnit.find(request)(mock)

            this >> s"received request: ${request.toString}"
            response match {
                case Some(resp) =>
                    this >> s"send response: ${resp.toString}"
                    outStream.writeBytes("HTTP/1.1 " + resp.code.key + "\r\n")
                    for ((key, value) <- resp.header) {
                        outStream.writeBytes(key + ": " + value + "\r\n")
                    }
                    outStream.writeBytes("\r\n")

                    resp.loadBody match {
                        case Some(content)  => outStream.write(content)
                        case None           =>
                    }
                    outStream.flush()
                case None =>
                    this >> s"send resource file ${request.resource}"
                    if (mock.baseDir.nonEmpty) {
                        outStream.writeBytes("HTTP/1.1 " + OK.key + "\r\n")
                        outStream.writeBytes("\r\n")
                        outStream.write(Files.readAllBytes(Paths.get(mock.baseDir + request.resource)))
                        outStream.flush()
                    }
                    else
                        sendError (s"no response found for request: ${request.toString}")
            }
        }
        catch {
            case e: Exception =>
                this error (null, e)
                sendError(e = e)
        }
        finally {
            this > stop
            connection.close()
        }
        channel
    }
    
}
