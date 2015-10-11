package org.mockit.core

import java.util.concurrent._

import org.mockit.logging.{Logger, NetworkLogger, LogChannel}
import org.mockit.mock.MockUnit
import org.mockit.mock.http.{HttpClientMockWorker, HttpClientMockUnit, HttpMockServer}
import org.mockit.mock.tcp.{TCPClientMockWorker, TCPMockServer, TCPMockUnit}
import org.mockit.mock.udp.{UDPMockUnit, UDPMockWorker}

/**
 * @author  pheymann
 * @version 0.1.0
 */
object MockAgent {

    /**
     * Creates and starts a ''Thread'' which runs the [[org.mockit.core.MockAgent]].
     *
     * @param config
     *              configuration of the [[org.mockit.mock.MockUnit]]
     * @param mockClass
     *              class of the [[org.mockit.mock.MockUnit]]
     * @return running [[org.mockit.core.MockAgent]] instance
     */
    def init(config: Configuration, mockClass: Class[_ <: MockUnit]): Thread = {
        val worker = new Thread(new MockAgent(config, mockClass))

        worker.start()
        worker
    }

}

/**
 * Instance which handles incoming [[org.mockit.mock.MockUnit]]s.
 *
 * Distinguishes between the different [[org.mockit.core.ConnectionType]]s
 * and runs and manages the matching worker.
 *
 * After execution finish the agent collects all log entries and sends
 * them back to the origin.
 *
 * @param config
 *              configuration of the [[org.mockit.mock.MockUnit]]
 * @param mockClass
 *              class of the [[org.mockit.mock.MockUnit]]
 */
class MockAgent (
                    val config:     Configuration,
                    val mockClass:  Class[_ <: MockUnit]
                ) extends   Runnable
                    with    NetworkLogger
                    with    Logger {

    protected override val mockName = classOf[MockAgent].getSimpleName
    protected override val mockType = BasicMockType.agent.toString

    private val isServer = {
        config.mockType match {
            case BasicMockType.server   => true
            case _                      => false
        }
    }
    private val poolSize = {
        if (isServer)
            1
        else
            config.threadNumber
    }

    private val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

    def run(): Unit = {
        val futures = new Array[Future[LogChannel]](poolSize)

        var repetition = 0
        var exception: Throwable = null

        this > s"$start for ${mockClass.getSimpleName}"
        try {
            do {
                config.mockConnection match {
                    case ConnectionType.tcp =>
                        if (isServer) {
                            this >> "initialize thread pool[0]: start tcp mock server"
                            futures(0) = pool.submit(new TCPMockServer(config, mockClass))
                        }
                        else {
                            this >> s"initialize thread pool[${config.threadNumber}]: start all tcp mock clients"
                            for (i <- 0 until config.threadNumber) {
                                val mock = mockClass.newInstance.asInstanceOf[TCPMockUnit]

                                futures(i) = pool.submit(new TCPClientMockWorker(config, mock))
                            }
                        }
                    case ConnectionType.udp =>
                        if (isServer) {
                            this >> "initialize thread pool[0]: start udp mock server"
                            val mock = mockClass.newInstance.asInstanceOf[UDPMockUnit]

                            futures(0) = pool.submit(new UDPMockWorker(config, mock))
                        }
                        else {
                            this >> s"initialize thread pool[${config.threadNumber}]: start all udp mock clients"
                            for (i <- 0 until config.threadNumber) {
                                val mock = mockClass.newInstance.asInstanceOf[UDPMockUnit]

                                futures(i) = pool.submit(new UDPMockWorker(config, mock))
                            }
                        }
                    case ConnectionType.http =>
                        if (isServer) {
                            this >> "initialize thread pool[0]: start http mock server"
                            futures(0) = pool.submit(new HttpMockServer(config, mockClass))
                        }
                        else {
                            this >> s"initialize thread pool[${config.threadNumber}]: start all http mock clients"
                            for (i <- 0 until config.threadNumber) {
                                val mock = mockClass.newInstance.asInstanceOf[HttpClientMockUnit]

                                futures(i) = pool.submit(new HttpClientMockWorker(config, mock))
                            }
                        }
                    case ConnectionType.none => this error "no valid connection type"
                }
                repetition += 1

                this >> "wait for all worker to shutdown and store logs"
                futures.foreach(future => channel ++= future.get)
            } while (repetition < config.repetitions)
        }
        catch {
            case e: Exception => exception = e
        }
        finally {
            pool.shutdown()
            if (!pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT))
                sendError("exception at system shutdown")
            if (exception != null)
                sendError(e = exception)
        }
        this >> s"send logs to mock origin [${config.originIp}]"
        NetworkLogger.sendLogsAndClear(config.originIp, channel)

        this > s"$stop - ${mockClass.getSimpleName}"
    }

}
