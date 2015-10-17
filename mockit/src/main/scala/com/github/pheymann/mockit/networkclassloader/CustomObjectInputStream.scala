package com.github.pheymann.mockit.networkclassloader

import java.io.ObjectInputStream
import java.io.InputStream
import java.io.ObjectStreamClass

/**
 * '''MockIt''' specific extension of the standard ''ObjectInputStream''.
 *
 * Is necessary to determine the network class loader as loader instance
 * for the input stream. Otherwise there will be ''ClassNotFoundExceptions''.
 *
 * @param classLoader
 *              instance used by the [[com.github.pheymann.mockit.networkclassloader.NetworkClassLoader]]
 * @param inStream
 *              standard input stream
 *
 * @author  pheymann
 * @version 0.1.0
 */
class CustomObjectInputStream(
                                val classLoader:    ClassLoader,
                                val inStream:       InputStream
                             ) extends ObjectInputStream(inStream) {

    override def resolveClass(desc: ObjectStreamClass): Class[_] = {
        classLoader.loadClass(desc.getName)
    }

}
