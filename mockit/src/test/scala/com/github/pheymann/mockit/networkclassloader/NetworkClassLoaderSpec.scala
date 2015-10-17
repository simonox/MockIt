package com.github.pheymann.mockit.networkclassloader

import java.net.Socket

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.mock.ProtocolMockUnit
import com.github.pheymann.mockit.util.testmock.{ClassLoaderTestMock, TestUDPMock, TestTCPMock}
import com.github.pheymann.mockit.util.{TestClass, NetworkClassLoadTestServer}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class NetworkClassLoaderSpec extends MockItSpec {

    "A NetworkClassLoader" should
        """transmit class binaries between the origin application
          |and a number of MockAgents. Furthermore the loader instanciate
          |the classes on the agents side and creates the class definition.
          |  He should be able to transmit a simple MockUnit with components.
        """.stripMargin in {

        val mockClass   = classOf[TestTCPMock]
        val component1  = classOf[TestUDPMock]
        val component2  = classOf[TestClass]

        val loader = new NetworkClassLoader
        val sender = new Thread(
            new NetworkClassLoadTestServer(
                mockClass,
                Map(
                    component1.getCanonicalName -> component1,
                    component2.getCanonicalName -> component2
                )
            )
        )
        sender.start()

        val client      = new Socket(DEFAULT_IP, MOCKIT_UPLOAD_PORT)
        val resultClass = loader.load(client).asInstanceOf[Class[_ <: ProtocolMockUnit]]

        val mockInst    = mockClass.newInstance
        val resultInst  = resultClass.newInstance

        checkInstances(mockInst, resultInst)

        sender.join()
    }

    it should "be able to transmit more complex MockUnit with sub classes" in {
        val mockClass   = classOf[ClassLoaderTestMock]

        val loader = new NetworkClassLoader
        val sender = new Thread(
            new NetworkClassLoadTestServer(
                mockClass,
                Map()
            )
        )
        sender.start()

        val client      = new Socket(DEFAULT_IP, MOCKIT_UPLOAD_PORT)
        val resultClass = loader.load(client).asInstanceOf[Class[_ <: ProtocolMockUnit]]

        val mockInst    = mockClass.newInstance
        val resultInst  = resultClass.newInstance

        checkInstances(mockInst, resultInst)

        sender.join()
    }

    it should "be able to transmit MockUnits without components" in {
        val mockClass   = classOf[TestTCPMock]

        val loader = new NetworkClassLoader
        val sender = new Thread(
            new NetworkClassLoadTestServer(mockClass, Map())
        )
        sender.start()

        val client = new Socket(DEFAULT_IP, MOCKIT_UPLOAD_PORT)
        val resultClass = loader.load(client).asInstanceOf[Class[_ <: ProtocolMockUnit]]

        val mockInst = mockClass.newInstance
        val resultInst = resultClass.newInstance

        checkInstances(mockInst, resultInst)

        sender.join()
    }

    private def checkInstances(expected: ProtocolMockUnit, actual: ProtocolMockUnit): Unit = {
        expected.getClass.getCanonicalName should be (actual.getClass.getCanonicalName )

        expected.waitOnData should be (actual.waitOnData)
        expected.isNext should be (actual.isNext)
        expected.mock._2 should be (actual.mock._2)
    }

}
