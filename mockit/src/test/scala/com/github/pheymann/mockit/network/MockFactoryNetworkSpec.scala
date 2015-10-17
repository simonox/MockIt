package com.github.pheymann.mockit.network

import java.util.concurrent.Executors

import com.github.pheymann.mockit.{MockItSpec, MockIt}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.core.{ConnectionType, ShutdownLatch}
import com.github.pheymann.mockit.util._
import com.github.pheymann.mockit.networkclassloader.ClassConverter
import com.github.pheymann.mockit.util.testmock.TestTCPMock

/**
 * @author  pheymann
 * @version 0.1.0
 */
class MockFactoryNetworkSpec extends MockItSpec {

    import ClassConverter._

    val testResponse    = Array[Byte](1, 2, 3, 4)

    val mockClass       = classOf[TestTCPMock]

    var serverIps:  ListBuffer[(String, String, Int)]   = _
    var shutdown:   ShutdownLatch                       = _
    var latch:      ShutdownLatch                       = _

    before {
        serverIps = new ListBuffer[(String, String, Int)]
        shutdown = new ShutdownLatch
        latch = new ShutdownLatch
    }

    after {
        serverIps.clear()
    }

    "A MockItFactory" should """provide procedures to acquire one remote MockAgent instance
                               |and run a client MockUnit on it""".stripMargin in {

        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ClientConfiguration(
                repetitions = 2,
                mockNumber = 1,
                mockConnection = ConnectionType.tcp
            )
        )

        val mockUnits = List(container)

        val pool    = Executors.newFixedThreadPool(3)
        val failure =   pool.submit(new DummyServer(2, testResponse))
                        pool.submit(new MockItDaemonDummy(1))

        shutdown.close
        val logsFuture = pool.submit(MockIt.mockNetwork(
            mockUnits,
            Map[String, Array[Byte]](),
            serverIps,
            shutdown,
            latch
        ))

        val logs = logsFuture.get
        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })

        serverIps.isEmpty should be (true)
        latch.continue should be (false)
        logs.isEmpty should be (true)
        failure.get(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT) should be (false)

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

    it should "be able to run a single server MockUnit" in {
        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                serverPort      = 1234,
                threadNumber    = 1,
                mockConnection  = ConnectionType.tcp
            )
        )

        val mockUnits = List(container)

        val pool = Executors.newFixedThreadPool(2)
            pool.submit(new MockItDaemonDummy(1))

        shutdown.close
        val logsFuture = pool.submit(MockIt.mockNetwork(
            mockUnits, Map[String, Array[Byte]](),
            serverIps,
            shutdown,
            latch
        ))
        latch.await()

        val logs = logsFuture.get
        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })

        serverIps.isEmpty should be (false)
        latch.continue should be (false)
        logs.isEmpty should be (true)

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

    it should "be able to run multiple MockUnits in the network" in {
        val container1 = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ClientConfiguration(
                repetitions = 2,
                mockNumber = 1,
                mockConnection = ConnectionType.tcp
            )
        )
        val container2 = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ClientConfiguration(
                mockNumber = 2,
                mockConnection = ConnectionType.tcp
            )
        )

        val mockUnits = List(container1, container2)

        val pool    = Executors.newFixedThreadPool(3)
        val failure =   pool.submit(new DummyServer(4, testResponse))
                        pool.submit(new MockItDaemonDummy(3))

        shutdown.close
        val logsFuture = pool.submit(MockIt.mockNetwork(
            mockUnits, Map[String, Array[Byte]](),
            serverIps,
            shutdown,
            latch
        ))

        val logs = logsFuture.get
        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })

        serverIps.isEmpty should be (true)
        latch.continue should be (false)
        logs.isEmpty should be (true)
        failure.get(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT) should be (false)

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

}