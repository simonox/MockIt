package com.github.pheymann.mockit.core

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.networkclassloader.ClassConverter
import com.github.pheymann.mockit.util.testmock.TestTCPMock

/**
 * @author  pheymann
 * @version 0.1.0
 */
class MockFactorySpec extends MockItSpec {

    import ClassConverter._

    val mockClass       = classOf[TestTCPMock]

    var shutdown:   ShutdownLatch                       = _
    var latch:      ShutdownLatch                       = _

    before {
        shutdown = new ShutdownLatch
        latch = new ShutdownLatch
    }

    "A MockFactory" should
        """provide procedures to create one local MockAgent instance
          |and run a MockUnit on it""".stripMargin in {

        val mockClass = classOf[TestTCPMock]
        val container = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                serverPort      = DEFAULT_PORT,
                threadNumber    = 1,
                mockConnection  = ConnectionType.tcp
            )
        )

        val mockUnits = List(container)

        shutdown.close
        val logs = MockFactory.local(mockUnits, shutdown, latch)

        for (log <- logs) {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        }
        logs.isEmpty should be (true)
    }

    it should "be able to create and run multiple MockAgents" in {
        val container1  = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                serverPort      = 10000,
                threadNumber    = 1,
                mockConnection  = ConnectionType.tcp
            )
        )
        val container2  = new MockUnitContainer(
            mockClass.getCanonicalName,
            mockClass,
            new ServerConfiguration(
                serverPort      = 10001,
                threadNumber    = 1,
                mockConnection  = ConnectionType.tcp
            )
        )

        val mockUnits = List(container1, container2)

        shutdown.close
        val logs = MockFactory.local(mockUnits, shutdown, latch)

        for (log <- logs) {
            println(log.toString)
            log.exception match {
                case Some(e) => e.printStackTrace()
                case None =>
            }
        }
        logs.isEmpty should be (true)
    }

}
