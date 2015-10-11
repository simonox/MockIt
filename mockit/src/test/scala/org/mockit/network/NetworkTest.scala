package org.mockit.network

import java.util.concurrent.Executors

import scala.collection.mutable.ListBuffer

import org.junit.Assert._
import org.junit.Test

import org.mockit.core._
import org.mockit.util._
import org.mockit.{MockIt, MockItBasicTest}
import org.mockit.networkclassloader.ClassConverter
import org.mockit.util.testmock.TestTCPMock

/**
 * @author  pheymann
 * @version 0.1.0
 */
class NetworkTest extends MockItBasicTest {

    import ClassConverter._

    @Test def testNetworkSingle(): Unit = {
        -- ("testNetworkSingle")

        val mockClass = classOf[TestTCPMock]
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
        val failure = pool.submit(new NetworkTestServer(2, TestTCPMock.testResponse))
                      pool.submit(new MockItMainClient(1))

        val serverIps = new ListBuffer[(String, String, Int)]
        val shutdown = new ShutdownLatch
        val latch = new ShutdownLatch

        shutdown.close
        val logsFuture = pool.submit(MockIt.mockNetwork(mockUnits, Map[String, Array[Byte]](), serverIps, shutdown, latch))

        assertTrue(serverIps.isEmpty)
        val logs = logsFuture.get
        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })
        assertTrue(logs.isEmpty)
        assertEquals(0, latch.getCount)
        assertFalse(failure.get(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT))

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

    @Test def testNetworkSingleServer(): Unit = {
        -- ("testNetworkSingle")

        val mockClass = classOf[TestTCPMock]
        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                1234,
                1,
                ConnectionType.tcp
            )
        )

        val mockUnits = List(container)

        val pool =  Executors.newFixedThreadPool(2)
                    pool.submit(new MockItMainClient(1))

        val serverIps = new ListBuffer[(String, String, Int)]
        val shutdown = new ShutdownLatch
        val latch = new ShutdownLatch

        shutdown.close
        val logsFuture = pool.submit(MockIt.mockNetwork(mockUnits, Map[String, Array[Byte]](), serverIps, shutdown, latch))

        latch.await()

        assertFalse(serverIps.isEmpty)
        val logs = logsFuture.get
        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })
        assertTrue(logs.isEmpty)
        assertEquals(0, latch.getCount)

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

    @Test def testNetworkMultiple(): Unit = {
        -- ("testNetworkMultiple")

        val mockClass = classOf[TestTCPMock]

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
        val failure = pool.submit(new NetworkTestServer(4, TestTCPMock.testResponse))
                      pool.submit(new MockItMainClient(3))

        val serverIps = new ListBuffer[(String, String, Int)]
        val shutdown = new ShutdownLatch
        val latch = new ShutdownLatch

        shutdown.close
        val logsFuture = pool.submit(MockIt.mockNetwork(mockUnits, Map[String, Array[Byte]](), serverIps, shutdown, latch))

        assertTrue(serverIps.isEmpty)
        val logs = logsFuture.get
        logs.foreach(log => {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        })
        assertTrue(logs.isEmpty)
        assertEquals(0, latch.getCount)
        assertFalse(failure.get(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT))

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

}
