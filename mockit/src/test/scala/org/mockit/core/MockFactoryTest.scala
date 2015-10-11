package org.mockit.core

import org.junit.Test
import org.junit.Assert._

import org.mockit.MockItBasicTest
import org.mockit.util.testmock.TestTCPMock

/**
 * @author  pheymann
 * @version 0.1.0
 */
class MockFactoryTest extends MockItBasicTest {

    import org.mockit.networkclassloader.ClassConverter._

    @Test def testLocalSingle(): Unit = {
        -- ("testLocalSingle")

        val latch     = new ShutdownLatch

        val mockClass = classOf[TestTCPMock]
        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                DEFAULT_PORT,
                1,
                ConnectionType.tcp
            )
        )

        val serverShutdown = new ShutdownLatch
        val mockUnits = List(container)

        serverShutdown.close
        val logs = MockFactory.local(mockUnits, serverShutdown, latch)

        for (log <- logs) {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        }
        assertTrue(logs.isEmpty)
    }

    @Test def testLocalMultiple(): Unit = {
        -- ("testLocalMultiple")

        val latch     = new ShutdownLatch

        val mockClass = classOf[TestTCPMock]

        val container1 = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                10000,
                1,
                ConnectionType.tcp
            )
        )
        val container2 = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                10001,
                1,
                ConnectionType.tcp
            )
        )

        val mockUnits = List(container1, container2)
        val serverShutdown = new ShutdownLatch

        serverShutdown.close
        val logs = MockFactory.local(mockUnits, serverShutdown, latch)

        for (log <- logs) {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        }
        assertTrue(logs.isEmpty)
    }

}
