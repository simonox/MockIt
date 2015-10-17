package com.github.pheymann.mockitjavaapi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.github.pheymann.mockit.core.*;
import com.github.pheymann.mockit.logging.LogEntry;
import com.github.pheymann.mockit.networkclassloader.DependencyResolver;
import com.github.pheymann.mockitjavaapi.core.*;

/**
 * Java wrapper for {@link com.github.pheymann.mockit.MockIt}. It is a global interface
 * to <strong>MockIt</strong> framework. It provides procedures
 * to run local and network mock ups.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JMockIt {

    /**
     * Creates a local mock up instance. All communication is
     * limited to the local machine.
     *
     * The instance gets shutdown by an external signal.
     *
     * @param mockUnits
     *              collection of all {@link com.github.pheymann.mockit.mock.MockUnit}s necessary for the mock up
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockLocal(
                                            List<MockUnitContainer>     mockUnits,
                                            ShutdownLatch serverShutdown,
                                            CountDownLatch              latch
                                          ) throws MockItException {
        return new JMockCallable() {
            @Override
            public List<LogEntry> call() throws MockItException {
                return com.github.pheymann.mockitjavaapi.core.ConvertUtil.scalaToJava(
                    MockFactory$.MODULE$.local(
                        ConvertUtil$.MODULE$.javaToScala(mockUnits),
                        serverShutdown,
                        latch
                    )
                );
            }
        };
    }

    /**
     * Creates a local mock up instance. All communication is
     * limited to the local machine.
     *
     * All necessary {@link com.github.pheymann.mockit.mock.MockUnit}s and {@link com.github.pheymann.mockit.core.Configuration}s get
     * collected dynamically via annotations.
     *
     * The instance gets shutdown by an external signal.
     *
     * @param mockKey
     *              key of the current mock up
     * @param source
     *              root directory to looking for <strong>MockIt</strong> annotations
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockLocal(
                                            String          mockKey,
                                            String          source,
                                            ShutdownLatch   serverShutdown,
                                            CountDownLatch  latch
                                         ) throws MockItException {
        return new JMockCallable() {
            @Override
            public List<LogEntry> call() throws MockItException, NoMockUnitConfigException {
                final DependencyResolver resolver = new DependencyResolver(source);

                return com.github.pheymann.mockitjavaapi.core.ConvertUtil.scalaToJava(
                    MockFactory$.MODULE$.local(
                        resolver.resolveMockUnits(mockKey),
                        serverShutdown,
                        latch
                    )
                );
            }
        };
    }

    /**
     * Java interface procedure to create a network mock up instance. This contains {@link com.github.pheymann.mockit.core.MockAgent}
     * acquisition and {@link com.github.pheymann.mockit.mock.MockUnit}, {@link com.github.pheymann.mockit.core.Configuration}
     * and components distribution. Furthermore the system lifecycle and logging management
     * is provided by the framework.
     *
     * @param mockUnits
     *              collection of all {@link com.github.pheymann.mockit.mock.MockUnit}
     * @param components
     *              components necessary to run the mock units
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockNetwork(
                                                List<MockUnitContainer>     mockUnits,
                                                Map<String, byte[]>         components,
                                                ShutdownLatch               serverShutdown,
                                                ShutdownLatch               latch
                                           ) throws MockItException {
        return new JMockCallable() {
            @Override
            public List<LogEntry> call() throws MockItException {
                return com.github.pheymann.mockitjavaapi.core.ConvertUtil.scalaToJava(
                    MockFactory$.MODULE$.networkServer(
                        ConvertUtil$.MODULE$.javaToScala(mockUnits),
                        ConvertUtil$.MODULE$.javaToScala(components),
                        serverIps,
                        serverShutdown,
                        latch
                    )
                );
            }
        };
    }

    /**
     * Creates a network mock up instance. This contains {@link com.github.pheymann.mockit.core.MockAgent}
     * acquisition and {@link com.github.pheymann.mockit.mock.MockUnit}, {@link com.github.pheymann.mockit.core.Configuration}
     * and components distribution. Furthermore the system lifecycle and logging management
     * is provided by the framework.
     *
     * All necessary {@link com.github.pheymann.mockit.mock.MockUnit}s, {@link com.github.pheymann.mockit.core.Configuration} and
     * depending components get collected dynamically via annotations.
     *
     * @param mockKey
     *              key of the current mock up
     * @param source
     *              root directory to looking for <strong>MockIt</strong> annotations
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws com.github.pheymann.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockNetwork(
        String          mockKey,
        String          source,
        ShutdownLatch   serverShutdown,
        ShutdownLatch   latch
    ) throws MockItException {
        return new JMockCallable() {
            @Override
            public List<LogEntry> call() throws MockItException, NoMockUnitConfigException {
                final DependencyResolver resolver = new DependencyResolver(source);

                return com.github.pheymann.mockitjavaapi.core.ConvertUtil.scalaToJava(
                    MockFactory$.MODULE$.networkServer(
                        resolver.resolveMockUnits(mockKey),
                        resolver.resolveComponents(),
                        serverIps,
                        serverShutdown,
                        latch
                    )
                );
            }
        };
    }

}
