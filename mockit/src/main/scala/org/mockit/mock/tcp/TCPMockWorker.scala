package org.mockit.mock.tcp

import java.net.Socket
import java.util.concurrent.Callable

import org.mockit.core.BasicMockType.BasicMockType
import org.mockit.core._
import org.mockit.logging.{LogChannel, Logger, NetworkLogger}

/**
 * Mock worker instance for [[org.mockit.mock.tcp.TCPMockUnit]].
 *
 * Receives a connection to a target server or client and executes
 * the protocol stored in the [[org.mockit.mock.tcp.TCPMockUnit]].
 *
 * If a failure is set the response/behaviour gets modified.
 *
 * @param connection
 *              connection to a target client or server
 * @param mock
 * @param _type
 *
 * @author  pheymann
 * @version 0.1.0
 */
class TCPMockWorker (
                        val connection:         Socket,
                        val mock:               TCPMockUnit,

                        val _type:  BasicMockType
                    )   extends  Callable[LogChannel]
                        with     NetworkLogger
                        with     Logger {

    override val mockName = mock.name
    override val mockType = _type.toString

    override def call: LogChannel = {
        this > s"$start@[${connection.getInetAddress.getHostAddress}]"
        try  {
            mock.init()

            while (mock.isNext) {
                if (mock.waitOnData) {
                    this >> "receive package"
                    mock.receiveData(connection.getInputStream)
                }

                val (response, failure) = mock.mock

                if (response != null || response.length > 0) {
                    this >> s"call mock: response size = ${response.length}; failure = $failure"
                    failure match {
                        case NoFault()                  => connection.getOutputStream.write(response, 0, response.length)
                        case FixedDelay(delay)          =>
                            Thread.sleep(delay)
                            connection.getOutputStream.write(response, 0, response.length)
                        case LooseConnection()          =>
                            connection.getOutputStream.close()
                            connection.close()
                        case MultipleResponses(factor)  =>
                            for (i <- 0 until factor) {
                                connection.getOutputStream.write(response, 0, response.length)
                            }
                        case wrong                      => sendWarn(s"current fault $wrong is not supported")
                    }
                }
                else {
                    this >> "call mock: no response"
                }
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
