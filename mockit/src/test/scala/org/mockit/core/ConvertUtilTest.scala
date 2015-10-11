package org.mockit.core

import java.util

import org.junit.Test
import org.junit.Assert._

import org.mockit._
import org.mockit.General.{JConnectionType, JFaultLevel, JBasicMockType}
import ConvertUtil._
import org.mockit.mock.tcp.TCPMockUnit
import org.mockit.util.JTestTCPMock

/**
 * @author  pheymann
 * @version 0.1.0
 */
class ConvertUtilTest extends MockItBasicTest {

    @Test def testJToSJBasicMockType(): Unit = {
        -- ("testJToSBasicMockType")

        assertEquals(BasicMockType.agent, javaToScala(JBasicMockType.agent))
        assertEquals(BasicMockType.client, javaToScala(JBasicMockType.client))
        assertEquals(BasicMockType.server, javaToScala(JBasicMockType.server))
    }

    @Test def testJToSJFaultLevel(): Unit = {
        -- ("testJToSFaultLevel")

        val delay = 10
        val jLevel = JFaultLevel.fixedDelay
        val jData = new JFaultLevelData(0, delay)

        assertEquals(FixedDelay(delay), javaToScala(jLevel, jData))
    }

    @Test def testJToSMockUnitConvert(): Unit = {
        -- ("testJToSMockUnitConvert")

        val jMock = new JTestTCPMock
        val sMock = jMock.asInstanceOf[TCPMockUnit]

        assertTrue(sMock.isNext)
        assertFalse(sMock.isNext)
        assertEquals(sMock.mock._2, NoFault())
    }

    @Test def testJtoSMockUnits(): Unit = {
        -- ("testJToSMockUnits")
        val jConfig = new JConfiguration

        jConfig.setMockType(JBasicMockType.server)
        jConfig.setMockConnection(JConnectionType.tcp)

        val jMockUnit = classOf[JTestTCPMock]
        val jContainer = new JMockUnitContainer(jMockUnit.getCanonicalName, jMockUnit, jConfig)
        val jMockUnits = new util.LinkedList[JMockUnitContainer]

        jMockUnits.add(jContainer)

        val mockUnits: List[MockUnitContainer] = jMockUnits

        assertEquals(jMockUnits.length, mockUnits.length)
        assertEquals(jMockUnits.getFirst.getMockName, mockUnits.head.mockName)
        assertEquals(jMockUnits.getFirst.getConfig.getMockNumber, mockUnits.head.config.mockNumber)
    }

}
