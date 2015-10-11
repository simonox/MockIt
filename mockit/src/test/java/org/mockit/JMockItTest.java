package org.mockit;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import org.mockit.core.*;
import org.mockit.core.JConfiguration;
import org.mockit.mock.MockUnit;
import org.mockit.util.JTestTCPMock;

import static org.junit.Assert.*;

/**
 * @author  pheymann
 * @version 0.1.0
 */
public class JMockItTest extends MockItBasicTest {

    @Test
    public void testMockLocal() throws Exception {
        printSeparator("testMockLocal");

        ShutdownLatch latch = new ShutdownLatch();

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Class<? extends MockUnit> mockUnit  = JTestTCPMock.class;

        JConfiguration config               = new JServerConfiguration(12345, 1, General.JConnectionType.tcp);
        JMockUnitContainer container        = new JMockUnitContainer(mockUnit.getCanonicalName(), mockUnit, config);
        List<JMockUnitContainer> mockUnits  = new LinkedList<>();

        mockUnits.add(container);

        ShutdownLatch serverShutdown = new ShutdownLatch();

        serverShutdown.close();

        assertTrue(pool.submit(JMockIt.mockLocal(mockUnits, serverShutdown, latch)).get().isEmpty());
    }

}
