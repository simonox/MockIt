package org.mockit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.mockit.core.JMockCallable;
import org.mockit.core.JMockUnitContainer;
import org.mockit.core.ShutdownLatch;
import org.mockit.core.MockItException;

/**
 * Java wrapper for {@link org.mockit.MockIt}. It is a global interface
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
     *              collection of all {@link org.mockit.mock.MockUnit}s necessary for the mock up
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws org.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockLocal(
                                            List<JMockUnitContainer>    mockUnits,
                                            ShutdownLatch               serverShutdown,
                                            CountDownLatch              latch
                                          )
    throws MockItException, NullPointerException {
        return MockIt.jMockLocal(mockUnits, serverShutdown, latch);
    }

    /**
     * Creates a local mock up instance. All communication is
     * limited to the local machine.
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
     * @throws org.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockLocal(
        String          mockKey,
        String          source,
        ShutdownLatch   serverShutdown,
        CountDownLatch  latch
    )
        throws MockItException, NullPointerException {
        return MockIt.jMockLocal(mockKey, source, serverShutdown, latch);
    }

    /**
     * Creates a network mock up instance. This contains {@link org.mockit.core.MockAgent}
     * acquisition and {@link org.mockit.mock.MockUnit}, {@link org.mockit.core.Configuration}
     * and components distribution. Furthermore the system lifecycle and logging management
     * is provided by the framework.
     *
     * All necessary {@link org.mockit.mock.MockUnit}s, {@link org.mockit.core.Configuration} and
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
     * @throws org.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockNetwork(
                                                String          mockKey,
                                                String          source,
                                                ShutdownLatch   serverShutdown,
                                                ShutdownLatch   latch
                                            )
    throws MockItException, NullPointerException {
        return MockIt.jMockNetwork(mockKey, source, serverShutdown, latch);
    }

    /**
     * Java interface procedure to create a network mock up instance. This contains {@link org.mockit.core.MockAgent}
     * acquisition and {@link org.mockit.mock.MockUnit}, {@link org.mockit.core.Configuration}
     * and components distribution. Furthermore the system lifecycle and logging management
     * is provided by the framework.
     *
     * @param mockUnits
     *              collection of all {@link org.mockit.mock.MockUnit}
     * @param components
     *              components necessary to run the mock units
     * @param serverShutdown
     *              shutdown signal
     * @param latch
     *              system ready signal
     * @throws org.mockit.core.MockItException
     *              wrapper exception which is thrown if an error occurs
     *              which can not be handled by the framework
     * @return mock up instance wrapped in a callable thread
     */
    public static JMockCallable mockNetwork(
                                                List<JMockUnitContainer>    mockUnits,
                                                Map<String, byte[]>         components,
                                                ShutdownLatch               serverShutdown,
                                                ShutdownLatch               latch
                                           )
    throws MockItException, NullPointerException {
        return MockIt.jMockNetwork(mockUnits, components, serverShutdown, latch);
    }

}
