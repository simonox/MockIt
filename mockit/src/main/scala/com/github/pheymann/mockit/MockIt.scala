package com.github.pheymann.mockit

import java.util.concurrent.{Callable, CountDownLatch}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.core.{MockFactory, MockItException, MockUnitContainer, ShutdownLatch}
import com.github.pheymann.mockit.logging.LogEntry
import com.github.pheymann.mockit.networkclassloader.DependencyResolver

/**
 * Global interface to '''MockIt''' framework. It provides procedures
 * to run local and network mock ups.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object MockIt {

    /**
     * Creates a local mock up instance. All communication is
     * limited to the local machine.
     *
     * The instance gets shutdown by an external signal.
     *
     * @param mockUnits
     *              collection of all [[com.github.pheymann.mockit.mock.MockUnit]]s necessary for the mock up
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    @throws(classOf[MockItException])
    def mockLocal(
                    mockUnits:      List[MockUnitContainer],
                    serverShutdown: ShutdownLatch,
                    latch:          CountDownLatch
                 ): Callable[ListBuffer[LogEntry]] = {
        new Callable[ListBuffer[LogEntry]] {
            override def call: ListBuffer[LogEntry] = {
                MockFactory.local(mockUnits, serverShutdown, latch)
            }
        }
    }

    /**
     * Creates a local mock up instance. All communication is
     * limited to the local machine.
     *
     * All necessary [[com.github.pheymann.mockit.mock.MockUnit]]s and [[com.github.pheymann.mockit.core.Configuration]] get
     * collected dynamically via annotations.
     *
     * The instance gets shutdown by an external signal.
     *
     * @param mockKey
     *              key of the current mock up
     * @param source
     *              root directory to looking for '''MockIt''' annotations
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    @throws(classOf[MockItException])
    def mockLocal(
                     mockKey:        String,
                     source:         String,
                     serverShutdown: ShutdownLatch,
                     latch:          CountDownLatch
                 ): Callable[ListBuffer[LogEntry]] = {
        new Callable[ListBuffer[LogEntry]] {
            override def call: ListBuffer[LogEntry] = {
                val resolver = new DependencyResolver(source)

                MockFactory.local(resolver.resolveMockUnits(mockKey), serverShutdown, latch)
            }
        }
    }

    /**
     * Creates a network mock up instance. This contains [[com.github.pheymann.mockit.core.MockAgent]]
     * acquisition and [[com.github.pheymann.mockit.mock.MockUnit]], [[com.github.pheymann.mockit.core.Configuration]]
     * and components distribution. Furthermore the system lifecycle and logging management
     * is provided by the framework.
     *
     * All necessary [[com.github.pheymann.mockit.mock.MockUnit]]s, [[com.github.pheymann.mockit.core.Configuration]] and
     * depending components get collected dynamically via annotations.
     *
     * @param mockKey
     *              key of the current mock up
     * @param source
     *              root directory to looking for '''MockIt''' annotations
     * @param serverIps
     *              collection of all [[com.github.pheymann.mockit.core.MockAgent]] address if their
     *              execute a mock server
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    @throws(classOf[MockItException])
    def mockNetwork(
                        mockKey:        String,
                        source:         String,
                        serverIps:      ListBuffer[(String, String, Int)],
                        serverShutdown: ShutdownLatch,
                        latch:          CountDownLatch
                   ): Callable[ListBuffer[LogEntry]] = {
        new Callable[ListBuffer[LogEntry]] {
            override def call: ListBuffer[LogEntry] = {
                val resolver = new DependencyResolver(source)

                MockFactory.networkServer(
                    resolver.resolveMockUnits(mockKey),
                    resolver.resolveComponents,
                    serverIps,
                    serverShutdown,
                    latch
                )
            }
        }
    }

    /**
     * Creates a network mock up instance. This contains [[com.github.pheymann.mockit.core.MockAgent]]
     * acquisition and [[com.github.pheymann.mockit.mock.MockUnit]], [[com.github.pheymann.mockit.core.Configuration]]
     * and components distribution. Furthermore the system lifecycle and logging management
     * is provided by the framework.
     *
     * @param mockUnits
     *              collection of all [[com.github.pheymann.mockit.mock.MockUnit]]s
     * @param components
     *              components necessary to run the mock units
     * @param serverIps
     *              collection of all [[com.github.pheymann.mockit.core.MockAgent]] address if their
     *              execute a mock server
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    @throws(classOf[MockItException])
    def mockNetwork(
                    mockUnits:      List[MockUnitContainer],
                    components:     Map[String, Array[Byte]],
                    serverIps:      ListBuffer[(String, String, Int)],
                    serverShutdown: ShutdownLatch,
                    latch:          CountDownLatch
                   ): Callable[ListBuffer[LogEntry]] = {
        new Callable[ListBuffer[LogEntry]] {
            override def call: ListBuffer[LogEntry] = {
                MockFactory.networkServer(mockUnits, components, serverIps, serverShutdown, latch)
            }
        }
    }

}
