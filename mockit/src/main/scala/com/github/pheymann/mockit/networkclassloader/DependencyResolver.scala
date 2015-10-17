package com.github.pheymann.mockit.networkclassloader

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

import org.reflections.Reflections

import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.core.Configuration
import com.github.pheymann.mockit.mock.MockUnit
import com.github.pheymann.mockit.annotation.{MockIt, MockItConfigs, MockItConfig, MockItComponent}

/**
 * Uses reflection api and reflections framework to find annotated classes.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object DependencyResolver {

    import ClassConverter._

    /**
     * Looks for [[com.github.pheymann.mockit.core.Configuration]]s for a given mock up
     * scenario (mock key and unit) in a configuration class.
     *
     * @see [[com.github.pheymann.mockit.annotation.MockItConfigs]]
     * @param mockKey
     *              key for a specific mock up
     * @param mockName
     *              name of a [[com.github.pheymann.mockit.mock.MockUnit]]
     * @param configClass
     *              class which defines configurations
     * @return configuration option
     */
    def findConfiguration(mockKey: String, mockName: String, configClass: Class[_]): Option[Configuration] = {
        var result: Configuration = null

        for {
            field <- configClass.getDeclaredFields

            if field.isAnnotationPresent(classOf[MockItConfig])
        } {
            val configAnnotation = field.getAnnotation(classOf[MockItConfig])

            if (mockKey.equals(configAnnotation.mockKey) && mockName.contains(configAnnotation.mockUnit())) {
                field.setAccessible(true)

                /* retrieve static field content */
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers))
                    result = field.get(null).asInstanceOf[Configuration]
                /* retrieve non static field content */
                else {
                    val inst = configClass.newInstance

                    result = field.get(inst).asInstanceOf[Configuration]
                }
                field.setAccessible(false)
            }
        }
        Option(result)
    }

    /**
     * Looks for all super classes and interfaces implemented by the
     * given mock class, excluding these classes/interfaces which belong
     * to the language packages or to this framework.
     *
     * @param mockClass
     *              class of [[com.github.pheymann.mockit.mock.MockUnit]]
     * @return super classes and interfaces mapped with their canonical names
     */
    def fetchSuperclasses(mockClass: Class[_]): mutable.HashMap[String, Array[Byte]] = {
        val components  = new mutable.HashMap[String, Array[Byte]]

        var superClass  = mockClass.getSuperclass
        var superName   = superClass.getCanonicalName

        /* exclude java, scala and mockit standard classes and interfaces */
        val pattern = "^[(java(x)?)(scala(x)?)(com.github.pheymann)](.)*"r

        while (
            superName match {
                case pattern(_*) => false
                case _ => true
            }
        ) {
            components += (superName -> superClass)

            superClass  = superClass.getSuperclass
            superName   = superClass.getCanonicalName

            for (interface <- superClass.getInterfaces) {
                components += (interface.getCanonicalName -> interface)
            }
        }
        components
    }

}

/**
 * Service provider for dynamic annotation based [[com.github.pheymann.mockit.mock.MockUnit]],
 * [[com.github.pheymann.mockit.core.Configuration]] and depending component class search.
 *
 * Uses reflection api and reflections framework to find annotated classes.
 *
 * @param source
 *              base directory for search
 *
 * @author  pheymann
 * @version 0.1.0
 */
class DependencyResolver(
                            val source: String
                        ) {

    import DependencyResolver._
    import ClassConverter._

    val components  = new mutable.HashMap[String, Array[Byte]]

    val resolver    = new Reflections(source)

    /**
     * Looks for [[com.github.pheymann.mockit.mock.MockUnit]] determined by the
     * given mock key and his [[com.github.pheymann.mockit.core.Configuration]]
     * and depending component classes.
     *
     * @see [[com.github.pheymann.mockit.annotation.MockIt]],
     * @see [[com.github.pheymann.mockit.annotation.MockItConfigs]],
     * @see [[com.github.pheymann.mockit.annotation.MockItConfig]]
     * @param mockKey
     *          key for the specific mock up
     * @throws com.github.pheymann.mockit.core.NoMockUnitConfigException
     *          thrown if no [[com.github.pheymann.mockit.core.Configuration]] is found for the [[com.github.pheymann.mockit.mock.MockUnit]]
     * @return container with mock unit and configuration
     */
    @throws(classOf[NoMockUnitConfigException])
    def resolveMockUnits(mockKey: String): List[MockUnitContainer] = {
        val units       = resolver.getTypesAnnotatedWith(classOf[MockIt]).toArray
        val configRefs  = resolver.getTypesAnnotatedWith(classOf[MockItConfigs]).toArray

        val mockUnits   = new ListBuffer[MockUnitContainer]

        var index = 0

        for {
            unit        <- units
            mockClass   = unit.asInstanceOf[Class[_ <: MockUnit]]
            mockName    = mockClass.getCanonicalName

            if mockClass.getAnnotation(classOf[MockIt]).mockKeys.contains(mockKey)
        } {
            components ++= fetchSuperclasses(mockClass)

            for {
                configRef   <- configRefs
                configClass = configRef.asInstanceOf[Class[_]]
                config      = findConfiguration(mockKey, mockName, configClass)
            } {
                 config match {
                    case Some(conf) =>
                        mockUnits += new MockUnitContainer(
                            mockName,
                            mockClass,
                            conf
                        )
                    case None =>
                }
            }

            index += 1
            if (mockUnits.size != index || !mockUnits.last.mockName.equals(mockName))
                throw new NoMockUnitConfigException(s"no configuration for mock unit $mockName")
        }
        mockUnits.toList
    }

    /**
     * Looks for depending component classes marked with the
     * [[com.github.pheymann.mockit.annotation.MockItComponent]] annotation.
     *
     * @return component classes
     */
    def resolveComponents: Map[String, Array[Byte]] = {
        val componentClasses = resolver.getTypesAnnotatedWith(classOf[MockItComponent]).toArray

        for {
            component <- componentClasses
            compClass = component.asInstanceOf[Class[_]]

            if !components.contains(compClass.getCanonicalName)
        } {
            val classArray: Array[Byte] = compClass

            components += (compClass.getCanonicalName -> classArray)
        }
        components.toMap
    }

}