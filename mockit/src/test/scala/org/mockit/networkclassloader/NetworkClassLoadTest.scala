package org.mockit.networkclassloader

import java.net.Socket

import org.junit.Assert._
import org.junit.Test

import org.mockit.MockItBasicTest
import org.mockit.core._
import org.mockit.mock.ProtocolMockUnit
import org.mockit.util.testmock.{ClassLoaderTestMock, TestUDPMock, TestTCPMock}
import org.mockit.util.{TestClass, NetworkClassLoadTestServer}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class NetworkClassLoadTest extends MockItBasicTest {

    @Test def testLoadClasses(): Unit = {
        -- ("testLoadClasses")

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

        val client = new Socket(DEFAULT_IP, MOCKIT_UPLOAD_PORT)
        val resultClass = loader.load(client).asInstanceOf[Class[_ <: ProtocolMockUnit]]

        val mockInst = mockClass.newInstance
        val resultInst = resultClass.newInstance

        checkInstances(mockInst, resultInst)

        sender.join()
    }

    @Test def testLoadSubClass(): Unit = {
        -- ("testLoadSubClass")

        val mockClass   = classOf[ClassLoaderTestMock]

        val loader = new NetworkClassLoader
        val sender = new Thread(
            new NetworkClassLoadTestServer(
                mockClass,
                Map()
            )
        )
        sender.start()

        val client = new Socket(DEFAULT_IP, MOCKIT_UPLOAD_PORT)
        val resultClass = loader.load(client).asInstanceOf[Class[_ <: ProtocolMockUnit]]

        val mockInst = mockClass.newInstance
        val resultInst = resultClass.newInstance

        checkInstances(mockInst, resultInst)

        sender.join()
    }

    @Test def testLoadZeroComponents(): Unit = {
        -- ("testLoadZeroClasses")

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
        assertEquals(expected.waitOnData, actual.waitOnData)
        assertEquals(expected.isNext, actual.isNext)
        assertEquals(expected.mock._2, actual.mock._2)
    }

}
