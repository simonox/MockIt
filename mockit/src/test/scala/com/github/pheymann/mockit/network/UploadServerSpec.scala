package com.github.pheymann.mockit.network

import com.github.pheymann.mockit.MockItSpec

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.networkclassloader.ClassConverter
import com.github.pheymann.mockit.util.testmock.TestTCPMock
import com.github.pheymann.mockit.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
class UploadServerSpec extends MockItSpec {

    import ClassConverter._

    "An UploadServer" should
        """send the binaries of one MockUnit and component classes to a
          |MockAgent which were acquired to process the mock-up. Furthermore
          |the server sends the configuration to the agent.
        """.stripMargin in {

        val shutdown    = new ShutdownLatch
        val serverIps   = new ListBuffer[(String, String, Int)]

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
        val server = UploadServer.init(mockUnits, Map(), serverIps, shutdown)

        /* loads binaries and configuration */
        val listener = new ServerListener

        val (resultConfig, resultClass) = listener.load(DEFAULT_IP)

        mockClass.getCanonicalName should be (resultClass.getCanonicalName)
        container.config should be (resultConfig)

        serverIps.isEmpty should be (true)

        server.join()

        shutdown.continue should be (false)
    }

    it should "be able to handle multiple MockUnits and MockAgents too" in {
        val shutdown    = new ShutdownLatch
        val serverIps   = new ListBuffer[(String, String, Int)]

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

        val mockUnits = List(container1, container2)
        val server = UploadServer.init(mockUnits, Map(), serverIps, shutdown)

        /* loads binaries and configuration */
        val listener = new ServerListener

        val (resultConfig, resultClass) = listener.load(DEFAULT_IP)

        mockClass1.getCanonicalName should be (resultClass.getCanonicalName)
        container1.config should be (resultConfig)
        shutdown.continue should be (true)

        val (resultConfig2, resultClass2) = listener.load(DEFAULT_IP)

        mockClass1.getCanonicalName should be (resultClass2.getCanonicalName)
        container1.config should be (resultConfig2)
        shutdown.continue should be (true)

        val (resultConfig3, resultClass3) = listener.load(DEFAULT_IP)

        mockClass2.getCanonicalName should be (resultClass3.getCanonicalName)
        container2.config should be (resultConfig3)

        serverIps.isEmpty should be (true)

        server.join()

        shutdown.continue should be (false)
    }

}