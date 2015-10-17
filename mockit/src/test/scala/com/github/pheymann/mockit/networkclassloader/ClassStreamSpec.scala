package com.github.pheymann.mockit.networkclassloader

import java.net.Socket
import java.util.concurrent.Executors

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.util.{ClassInputStreamServer, TestClass}

/**
 * Tests the [[ClassInputStream]] and
 * [[com.github.pheymann.mockit.networkclassloader.ClassOutputStream]].
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ClassStreamSpec extends MockItSpec {

    import ClassConverter._

    "ClassInputStream and ClassOutputStream" should
        """form a communication channel
          |to transmit class binaries between the origin application and
          |a number of MockAgents.
        """.stripMargin in {

        val mockClass               = classOf[TestClass]
        val classBytes: Array[Byte] = mockClass

        val pool    = Executors.newFixedThreadPool(1)
        val failure = pool.submit(new ClassInputStreamServer(mockClass.getCanonicalName, classBytes))

        val client = new Socket(DEFAULT_IP, DEFAULT_PORT)

        ClassOutputStream.send(classBytes, mockClass.getCanonicalName, client)

        failure.get(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT) should be (false)

        pool.shutdown()
        pool.awaitTermination(DEFAULT_SHUTDOWN_TIME, DEFAULT_SHUTDOWN_UNIT)
    }

}
