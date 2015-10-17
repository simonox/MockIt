package com.github.pheymann.mockit.util

import java.net.ServerSocket
import java.util.concurrent.Callable

import com.github.pheymann.mockit.core.core._
import com.github.pheymann.mockit.networkclassloader.ClassInputStream
import com.github.pheymann.mockit.util.TestUtil._

class ClassInputStreamServer(
                                val expectedName:   String,
                                val expectedClass:  Array[Byte]
                            ) extends Callable[Boolean] {

    val server = new ServerSocket(DEFAULT_PORT)

    override def call: Boolean = {
        var failure = false

        val client = server.accept

        val (name, classBytes) = ClassInputStream.receive(client)

        client.close()
        server.close()

        failure = !expectedName.equals(name)
        failure = !checkArrayEquals(expectedClass, classBytes)
        failure
    }

}
