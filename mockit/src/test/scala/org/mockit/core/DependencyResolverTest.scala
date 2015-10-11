package org.mockit.core

import org.junit.Test
import org.junit.Assert._

import org.mockit.MockItBasicTest
import org.mockit.networkclassloader.DependencyResolver
import org.mockit.util.TestClass
import org.mockit.util.testmock.{TestConfig, TestAnnotationMock}

/**
 * @author  pheymann
 * @version 0.1.0
 */
class DependencyResolverTest extends MockItBasicTest {

    @Test def testFindConfiguration(): Unit = {
        val mockName = classOf[TestAnnotationMock].getSimpleName

        val configOpt = DependencyResolver.findConfiguration("annotation-test", mockName, classOf[TestConfig])
        val config = {
            configOpt match {
                case Some(conf) => conf
                case None => null
            }
        }
        assertNotNull(config)
        assertEquals(1, config.serverPort)
        assertEquals(BasicMockType.server, config.mockType)
        assertEquals(ConnectionType.tcp, config.mockConnection)
    }

    @Test def testFetchSuperclasses(): Unit = {
        var components = DependencyResolver.fetchSuperclasses(classOf[TestAnnotationMock])

        assertEquals(0, components.size)

        /* TODO test positive case
        components = DependencyResolver.fetchSuperclasses(classOf[TestTCPMockFixedDelay])

        assertEquals(1, components.size)
        assertEquals(classOf[BasicTCPTestMock].getCanonicalName, components.head._1)
        */
    }

    @Test def testResolveMockUnit(): Unit = {
        -- ("testResolveMockUnit")

        val resolver = new DependencyResolver("org.mockit")

        val mockUnits = resolver.resolveMockUnits("annotation-test")

        assertEquals(1, mockUnits.length)

        val container = mockUnits.head

        assertEquals(container.config.serverPort, 1)
        assertEquals(container.config.mockNumber, 1)
    }

    @Test def testResolveMockUnitWithSuperclass(): Unit = {
        -- ("testResolveMockUnitWithSuperclass")

        val resolver = new DependencyResolver("org.mockit")

        val mockUnits   = resolver.resolveMockUnits("dr-test")
        val components  = resolver.resolveComponents

        assertEquals(1, components.size)
        assertEquals(1, mockUnits.length)

        val container = mockUnits.head

        assertEquals(container.config.serverPort, 1)
        assertEquals(container.config.mockNumber, 1)
    }

    @Test def testResolveComponents(): Unit = {
        -- ("testResolveComponents")

        val resolver = new DependencyResolver("org.mockit")

        val mockUnits   = resolver.resolveMockUnits("annotation-test")
        val components  = resolver.resolveComponents

        assertEquals(1, components.size)

        val (name, classBytes) = components.head

        assertEquals(name, classOf[TestClass].getCanonicalName)
    }

}
