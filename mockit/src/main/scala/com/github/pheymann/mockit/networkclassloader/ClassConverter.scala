package com.github.pheymann.mockit.networkclassloader

import java.io._

import scala.collection.mutable.HashMap

import org.apache.commons.io.IOUtils

import com.github.pheymann.mockit.networkclassloader.networkclassloader._

/**
 * Collection of implicit conversion functions to map
 * a class definition to a byte array.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object ClassConverter {

    implicit def toByteArray(clazz: Class[_]): Array[Byte] = {
        val loader = clazz.getClassLoader

        IOUtils.toByteArray(loader.getResourceAsStream(path(clazz)))
    }

    implicit def toByteArray(component: (String, Class[_])): (String, Array[Byte]) = {
        (component._1, component._2)
    }

    implicit def toByteArrayTuple(components: Map[String, Class[_]]): Map[String, Array[Byte]] = {
        val convert = new HashMap[String, Array[Byte]]()

        for (component <- components)
            convert += component
        convert.toMap
    }

    def path(clazz: Class[_]): String = {
        clazz.getCanonicalName.replace(PACKAGE_SEPARATOR, File.separator) + CLASS_ENDING
    }

}
