package com.github.pheymann.mockit.network

import com.github.pheymann.mockit.MockItSpec

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.core.ConnectionType
import com.github.pheymann.mockit.networkclassloader.ClassConverter
import com.github.pheymann.mockit.util.testmock.TestTCPMock
import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.util.TestClass

/**
 * Tests the integration between the different server types.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ServerIntegrationSpec extends MockItSpec {

    import ClassConverter._

    "All server types" should
        """form a communication interface to manage the
          |mock-up system""".stripMargin in {

        val serverIps   = new ListBuffer[(String, String, Int)]
        val shutdown    = new ShutdownLatch

        val mockClass = classOf[TestTCPMock]
        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ClientConfiguration(
                mockNumber      = 1,
                mockConnection  = ConnectionType.tcp
            )
        )
        val mockUnits = List(container)

        val testClass   = classOf[TestClass]
        val components  = Map(testClass.getCanonicalName -> testClass)

        val multiServer     = InvitationServer.init(shutdown)
        val uploadServer    = UploadServer.init(mockUnits, components, serverIps, shutdown)

        val listener = new ServerListener

        val (resultConfig, resultClass) = listener.load(DEFAULT_IP)

        mockClass.getCanonicalName should be (resultClass.getCanonicalName)
        container.config should be (resultConfig)

        serverIps.isEmpty should be (true)

        multiServer.join()
        uploadServer.join()
    }

}