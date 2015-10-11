package org.mockit.util

import org.junit.Assert._

import org.mockit.core.Configuration

object TestUtil {

    def checkArrayEquals(expected: Array[Byte], actual: Array[Byte]): Boolean = {
        if (expected.length != actual.length)
            false
        else {
            var equal = true

            for (i <- 0 until expected.length)
                equal &&= expected(i) == actual(i)
            equal
        }
    }

    def assertConfigurationEquals(expected: Configuration, actual: Configuration): Unit = {
        assertEquals(expected.threadNumber, actual.threadNumber)
        assertEquals(expected.repetitions, actual.repetitions)
        assertEquals(expected.serverPort, actual.serverPort)
        assertEquals(expected.targetPort, actual.targetPort)
        assertEquals(expected.targetIp, actual.targetIp)
        assertEquals(expected.originIp, actual.originIp)
        assertEquals(expected.mockNumber, actual.mockNumber)
        assertEquals(expected.mockType, actual.mockType)
    }

}
