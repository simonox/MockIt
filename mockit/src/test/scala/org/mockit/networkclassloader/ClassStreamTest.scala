package org.mockit.networkclassloader

import java.net.Socket
import java.util.concurrent.Executors

import org.junit.Test
import org.junit.Assert._

import org.mockit.util.testmock.TestTCPMock
import org.mockit.MockItBasicTest
import org.mockit.util.{ClassStreamTestServer, TestClass}
import org.mockit.core._

/**
 * Tests the [[org.mockit.networkclassloader.ClassInputStream]] and
 * [[org.mockit.networkclassloader.ClassOutputStream]].
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ClassStreamTest extends MockItBasicTest {

    import org.mockit.networkclassloader.ClassConverter._

    @Test def testSendAndReceive(): Unit = {
        -- ("testSendAndReceive")

        val mockClass = classOf[TestClass]
        val classBytes: Array[Byte] = mockClass

        val pool    = Executors.newFixedThreadPool(1)
        val failure = pool.submit(new ClassStreamTestServer(mockClass.getCanonicalName, classBytes))

        val client = new Socket(DEFAULT_IP, DEFAULT_PORT)

        ClassOutputStream.send(classBytes, mockClass.getCanonicalName, client)

        assertFalse(failure.get(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT))
        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

}
