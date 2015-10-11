package org.mockit.network

import scala.collection.mutable.ListBuffer

import org.junit.Assert._
import org.junit.Test

import org.mockit.util.testmock.TestTCPMock
import org.mockit.MockItBasicTest
import org.mockit.core._
import org.mockit.util.TestUtil._

/**
 * @author  pheymann
 * @version 0.1.0
 */
class UploadServerTest extends MockItBasicTest {

    import org.mockit.networkclassloader.ClassConverter._

    @Test def testSendSingleMockUnit(): Unit = {
        -- ("testSendSingleMockUnit")

        val shutdown = new ShutdownLatch

        val mockClass = classOf[TestTCPMock]

        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ClientConfiguration(
                mockNumber      = 1,
                mockConnection  = ConnectionType.tcp
            )
        )

        val serverIps = new ListBuffer[(String, String, Int)]

        val mockUnits = List(container)
        val server = UploadServer.init(mockUnits, Map(), serverIps, shutdown)

        val listener = new ServerListener

        val (resultConfig, resultClass) = listener.load(DEFAULT_IP)

        assertEquals(mockClass.getCanonicalName, resultClass.getCanonicalName)
        assertConfigurationEquals(container.config, resultConfig)

        assertTrue(serverIps.isEmpty)

        server.join()
        assertFalse(shutdown.continue)
    }

    @Test def testSendMultipleMockUnits(): Unit = {
        -- ("testSendMultipleMockUnits")

        val shutdown = new ShutdownLatch

        val mockClass1 = classOf[TestTCPMock]
        val mockClass2 = classOf[TestTCPMock]

        val container1 = new MockUnitContainer(
            mockClass1.getCanonicalName,
            mockClass1,
            new ClientConfiguration(
                mockNumber      = 2,
                mockConnection  = ConnectionType.tcp
            )
        )
        val container2 = new MockUnitContainer(
            mockClass2.getCanonicalName,
            mockClass2,
            new ClientConfiguration(
                mockNumber      = 1,
                mockConnection  = ConnectionType.tcp
            )
        )

        val serverIps = new ListBuffer[(String, String, Int)]

        val mockUnits = List(container1, container2)

        val server = UploadServer.init(mockUnits, Map(), serverIps, shutdown)

        val listener = new ServerListener

        val (resultConfig, resultClass) = listener.load(DEFAULT_IP)

        assertEquals(mockClass1.getCanonicalName, resultClass.getCanonicalName)
        assertConfigurationEquals(container1.config, resultConfig)
        assertTrue(shutdown.continue)

        val (resultConfig2, resultClass2)= listener.load(DEFAULT_IP)

        assertEquals(mockClass1.getCanonicalName, resultClass2.getCanonicalName)
        assertConfigurationEquals(container1.config, resultConfig2)
        assertTrue(shutdown.continue)

        val (resultConfig3, resultClass3) = listener.load(DEFAULT_IP)

        assertEquals(mockClass2.getCanonicalName, resultClass3.getCanonicalName)
        assertConfigurationEquals(container2.config, resultConfig3)

        assertTrue(serverIps.isEmpty)

        server.join()
        assertFalse(shutdown.continue)
    }

}
