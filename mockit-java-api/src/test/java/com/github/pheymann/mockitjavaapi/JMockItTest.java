package com.github.pheymann.mockitjavaapi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

import com.github.pheymann.mockitjavaapi.core.ConfigurationFactory;
import com.github.pheymann.mockitjavaapi.core.MockUnitContainerFactory;
import org.junit.Before;
import org.junit.Test;

import com.github.pheymann.mockit.annotation.MockItConfigs;
import com.github.pheymann.mockit.core.*;
import com.github.pheymann.mockit.annotation.MockItConfig;
import com.github.pheymann.mockit.mock.MockUnit;
import com.github.pheymann.mockitjavaapi.util.JTestTCPMock;

/**
 * @author  pheymann
 * @version 0.1.0
 */
@MockItConfigs
public class JMockItTest extends MockItBasicTest {

    @MockItConfig(mockKey = "test-key", mockUnit = "JTestTCPMock")
    private final Configuration config = ConfigurationFactory.createServer(1, 10000, General.JConnectionType.tcp);

    @Before
    public void init() {
        setTestName(JMockItTest.class.getSimpleName());
    }

    @Test
    public void testMockLocal() throws Exception {
        printSeparator("testMockLocal");

        final ShutdownLatch serverShutdown  = new ShutdownLatch();
        final ShutdownLatch latch           = new ShutdownLatch();

        final ExecutorService pool = Executors.newSingleThreadExecutor();

        final Class<? extends MockUnit> mockUnit  = JTestTCPMock.class;

        final Configuration config = ConfigurationFactory.createServer(1, 10000, General.JConnectionType.tcp);

        final MockUnitContainer container   = MockUnitContainerFactory.create(mockUnit.getCanonicalName(), mockUnit, config);
        final List<MockUnitContainer>  mockUnits    = new LinkedList<>();

        mockUnits.add(container);
        serverShutdown.close();

        assertTrue(pool.submit(JMockIt.mockLocal(mockUnits, serverShutdown, latch)).get().isEmpty());
    }

    @Test
    public void testMockLocalWithAnnotations() throws Exception {
        printSeparator("testMockLocalWithAnnotations");

        final ShutdownLatch serverShutdown  = new ShutdownLatch();
        final ShutdownLatch latch           = new ShutdownLatch();

        final ExecutorService pool = Executors.newSingleThreadExecutor();

        serverShutdown.close();

        assertTrue(pool.submit(JMockIt.mockLocal("test-key", "", serverShutdown, latch)).get().isEmpty());
    }

}
