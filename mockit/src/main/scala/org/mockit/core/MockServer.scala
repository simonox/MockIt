package org.mockit.core

import java.io.ObjectInputStream
import java.net.{Socket, SocketTimeoutException, ServerSocket}
import java.util.concurrent.{Future, Executors, ExecutorService, Callable}

import org.mockit.logging.{Logger, NetworkLogger, LogChannel}
import org.mockit.mock.MockUnit

import scala.collection.mutable.ListBuffer

abstract class MockServer (
                              val config:     Configuration,
                              val mockClass:  Class[_ <: MockUnit]
                          ) extends Callable[LogChannel]
                                    with    NetworkLogger
                                    with    Logger {

    val server = new ServerSocket(config.serverPort)

    protected override val mockType = BasicMockType.server.toString

    protected val pool: ExecutorService = Executors.newFixedThreadPool(config.threadNumber)

    def handle(connection: Socket): Future[LogChannel]

    override def call: LogChannel = {
        val logs = new ListBuffer[Future[LogChannel]]

        var continue = true

        >> ("wait until mock server system is online")
        server.accept.close
        server.setSoTimeout(SERVER_TIMEOUT)

        > (s"${start}@${config.serverPort}")
        do {
            try {
                logs += handle(server.accept)
            }
            catch {
                case e: SocketTimeoutException => >> ("server listening interruption occurred")
                case e: Throwable => error ("can't create mock unit or socket error", e)
            }

            >> ("request 'keep alive' message")
            try {
                val client = new Socket(config.originIp, MOCKIT_SD_PORT)

                continue = new ObjectInputStream(client.getInputStream).readBoolean
            }
            catch {
                case e: Throwable => {
                    > ("shutdown-server not reachable > shutdown")
                    continue = false
                }
            }
        } while (continue)

        >> ("shutdown thread pool and store logs")
        pool.shutdown
        logs.foreach(log => {
            channel ++= log.get
        })
        if (!pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)) {
            sendError( "exception at system shutdown")
        }
        > (stop)

        server.close
        channel
    }
    
}
