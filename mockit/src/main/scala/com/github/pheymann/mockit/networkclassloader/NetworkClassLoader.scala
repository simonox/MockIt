package com.github.pheymann.mockit.networkclassloader

import java.net._

import com.github.pheymann.mockit.core.Configuration
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.logging.Logger

/**
 * '''MockIt''' class loader implementation to handle class
 * definitions which are sent via a network.
 *
 * @param classLoader
 *              standard class loader used by this instance
 *
 * @author  pheymann
 * @version 0.1.0
 */
class NetworkClassLoader(
                            val classLoader: ClassLoader = classOf[NetworkClassLoader].getClassLoader
                        )   extends ClassLoader (classLoader)
                            with    Logger {

    /**
     * Loads the [[com.github.pheymann.mockit.mock.MockUnit]], his [[com.github.pheymann.mockit.core.Configuration]]
     * and component classes from the [[com.github.pheymann.mockit.network.UploadServer]] and
     * defines them.
     *
     * @param connection
     *              connection to the target [[com.github.pheymann.mockit.network.UploadServer]]
     * @return class of the [[com.github.pheymann.mockit.mock.MockUnit]]
     */
    def load(connection: Socket): Class[_ <: MockUnit] = {
        val address = connection.getInetAddress.getHostAddress

        val componentNumber = ClassInputStream.componentNumber(connection)

        this >> s"[$address]: load and define $componentNumber component classes"
        for (index <- 0 until componentNumber) {
            val (name, classBytes) = ClassInputStream.receive(connection)

            define(classBytes, name)
            this >> s"[$address]: component class $componentNumber loaded"
        }

        this >> s"[$address]: load and define mock unit"
        val (mockName, mockBytes) = ClassInputStream.receive(connection)
        val mockUnit = define(mockBytes, mockName)

        mockUnit.asInstanceOf[Class[_ <: MockUnit]]
    }

    /**
     * Defines the given class with the standard class loader. If the
     * class is already available, it is loaded from memory.
     *
     * @param clazz
     * @param name
     *              canonical name
     * @return class instance
     */
    private def define(clazz: Array[Byte], name: String): Class[_] = {
        var result: Class[_] = null

        try {
            result = defineClass(name, clazz, 0, clazz.length)
        }
        catch {
            case e: LinkageError =>
                this > s"class $name is already available"
                result = this.loadClass(name)

                if (result == null)
                    throw e
            case e: Throwable => throw e
        }
        result
    }

}