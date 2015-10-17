package com.github.pheymann.mockit.mock.http

import java.io.{DataOutputStream, InputStreamReader, BufferedReader}
import java.net.Socket
import java.util.concurrent.Callable


import com.github.pheymann.mockit.core.{BasicMockType, Configuration}
import com.github.pheymann.mockit.logging.{NetworkLogger, Logger, LogChannel}

/**
 * Mock worker instance for [[HttpClientMockUnit]].
 *
 * Opens a socket to the target server and executes the requests stored
 * in the [[HttpClientMockUnit]]. Every response
 * gets proofed against the expected result. If a deviation exists it
 * gets logged.
 *
 * By now ''keep-alive'' configurations are not supported.
 *
 * @param config
 *              mock unit configuration
 * @param mock
 *              mock unit
 *
 * @author  pheymann
 * @version 0.1.0
 */
class HttpClientMockWorker (
                               val config:  Configuration,
                               val mock:    HttpClientMockUnit
                           )    extends  Callable[LogChannel]
                                with     NetworkLogger
                                with     Logger {

    import HttpResponse._

    override val mockName = mock.name
    override val mockType = BasicMockType.client.toString

    var connection: Socket = null

    override def call: LogChannel = {
        // TODO pheymann implement persistent connection handling
        var keepAlive = false

        try {
            mock.init()

            for ((request, response) <- mock.mock) {
                if (!keepAlive) {
                    connection = new Socket(config.targetIp, config.targetPort)

                    this > s"$start@[${connection.getInetAddress.getHostAddress}]"
                }

                this >> s"send request: ${request.toString}"
                val outStream = new DataOutputStream(connection.getOutputStream)

                outStream.writeBytes(request.method.toString + " " + request.resource + " HTTP/1.1\r\n")
                for ((key, value) <- response.header) {
                    outStream.writeBytes(key + ": " + value + "\r\n")
                }
                outStream.writeBytes("\r\n")

                request.loadBody match {
                    case Some(content)  => outStream.write(content)
                    case None           =>
                }
                outStream.flush()

                val inStream    = new BufferedReader(new InputStreamReader(connection.getInputStream))
                val primaries   = inStream.readLine.split(" ")
                val actual      = HttpResponse.generate(primaries(1), inStream)

                this >> "proof response"
                if (!response.equals(actual))
                    this sendError
                        s"""request:  ${request.toString}\n
                           |expected: ${response.toString}\n
                           |actual:   ${actual.toString}""".stripMargin

                if (!keepAlive)
                    connection.close()
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
