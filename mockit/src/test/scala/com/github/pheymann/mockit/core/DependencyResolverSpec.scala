package com.github.pheymann.mockit.core

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.networkclassloader.DependencyResolver
import com.github.pheymann.mockit.util.testmock.{TestConfig, TestAnnotationMock}
import com.github.pheymann.mockit.networkclassloader.DependencyResolver
import com.github.pheymann.mockit.util.TestClass
import com.github.pheymann.mockit.util.testmock.{TestConfig, TestAnnotationMock}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class DependencyResolverSpec extends MockItSpec {

    val mockKey = "annotation-test"

    "A DependencyResolver" should "find annotated Configurations by mock-key and MockUnit name" in  {
        val mockName    = classOf[TestAnnotationMock].getSimpleName

        val configOpt = DependencyResolver.findConfiguration(mockKey, mockName, classOf[TestConfig])
        val config = {
            configOpt match {
                case Some(conf) => conf
                case None => null
            }
        }

        val expected = (new TestConfig).config

        config should not be null
        config should be (expected)
    }

    it should "find annotated component classes" in {
        val resolver = new DependencyResolver("com.github.pheymann")

        val components  = resolver.resolveComponents

        components.size should be (1)

        val (name, classBytes) = components.head

        name should be (classOf[TestClass].getCanonicalName)
    }

    it should "find MockUnits by mock-key" in {
        val resolver = new DependencyResolver("com.github.pheymann")

        val mockUnits = resolver.resolveMockUnits(mockKey)

        mockUnits.size should be(1)

        val container = mockUnits.head
        val config = (new TestConfig).config

        container.mockName should be(classOf[TestAnnotationMock].getCanonicalName)
        container.config should be(config)
    }

    it should "find all super-classes and interfaces of a MockUnit"  in {
        val mockKey = "dr-test"

        val resolver = new DependencyResolver("com.github.pheymann")

        val mockUnits   = resolver.resolveMockUnits(mockKey)
        val components  = resolver.resolveComponents

        mockUnits.size should be (1)
        components.size should be (1)

        val (name, classBytes) = components.head

        name should be (classOf[TestClass].getCanonicalName)
    }

}
