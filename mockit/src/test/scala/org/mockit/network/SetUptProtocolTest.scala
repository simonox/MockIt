package org.mockit.network

import scala.collection.mutable.ListBuffer

import org.junit.Assert._
import org.junit.Test

import org.mockit.MockItBasicTest
import org.mockit.util.testmock.TestTCPMock
import org.mockit.util.TestUtil._
import org.mockit.core._
import org.mockit.util.TestClass

/**
 * Tests the integration between the different server types.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class SetUptProtocolTest extends MockItBasicTest {

    import org.mockit.networkclassloader.ClassConverter._

    @Test def testSetUp(): Unit = {
        -- ("testSetUp")

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

        val testClass = classOf[TestClass]
        val components = Map(testClass.getCanonicalName -> testClass)

        val multiServer = InvitationServer.init(shutdown)
        val uploadServer = UploadServer.init(mockUnits, components, serverIps, shutdown)

        val listener = new ServerListener

        val (resultConfig, resultClass) = listener.load(DEFAULT_IP)

        assertEquals(mockClass.getCanonicalName, resultClass.getCanonicalName)
        assertConfigurationEquals(container.config, resultConfig)

        assertTrue(serverIps.isEmpty)

        multiServer.join()
        uploadServer.join()
    }

}
