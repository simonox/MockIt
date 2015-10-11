package org.mockit.util

import java.net.ServerSocket
import java.util.concurrent.Callable

import org.mockit.core._
import org.mockit.networkclassloader.ClassInputStream
import org.mockit.util.TestUtil._

class ClassStreamTestServer(
                            val expectedName:   String,
                            val expectedClass:  Array[Byte]
                           ) extends Callable[Boolean] {

    val server = new ServerSocket(DEFAULT_PORT)

    override def call: Boolean = {
        var failure = false

        val client = server.accept

        val (name, classBytes) = ClassInputStream.receive(client)

        client.close
        server.close

        failure = !expectedName.equals(name)
        failure = !checkArrayEquals(expectedClass, classBytes)
        failure
    }

}
