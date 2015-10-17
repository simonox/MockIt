package com.github.pheymann.mockitjavaapi.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import scala.Tuple2;

import com.github.pheymann.mockit.core.*;

import com.github.pheymann.mockitjavaapi.General;
import com.github.pheymann.mockitjavaapi.MockItBasicTest;

/**
 * @author  pheymann
 * @version 0.1.0
 */
public class ConvertUtilTest extends MockItBasicTest {

    @Before
    public void init() {
        setTestName(ConvertUtilTest.class.getSimpleName());
    }

    @Test
    public void testJToSJBasicMockType() {
        printSeparator("testJToSBasicMockType");

        assertEquals(BasicMockType.agent(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JBasicMockType.agent));
        assertEquals(BasicMockType.client(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JBasicMockType.client));
        assertEquals(BasicMockType.server(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JBasicMockType.server));
        assertEquals(BasicMockType.p2p(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JBasicMockType.p2p));
        assertEquals(BasicMockType.none(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JBasicMockType.none));
    }

    @Test
    public void testJToSJConnectionType() {
        printSeparator("testJToSJConnectionType");

        assertEquals(ConnectionType.tcp(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JConnectionType.tcp));
        assertEquals(ConnectionType.udp(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JConnectionType.udp));
        assertEquals(ConnectionType.http(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JConnectionType.http));
        assertEquals(ConnectionType.none(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JConnectionType.none));
    }

    @Test
    public void testJToSJFaultLevel() {
        printSeparator("testJToSFaultLevel");

        final int delay     = 10;
        final int factor    = 2;

        final JFaultLevelData jData = new JFaultLevelData(factor, delay);

        assertEquals(new NoFault(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JFaultLevel.noFault, jData));
        assertEquals(new FixedDelay(delay), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JFaultLevel.fixedDelay, jData));
        assertEquals(new LooseResponse(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JFaultLevel.looseResponse, jData));
        assertEquals(new MultipleResponses(factor), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JFaultLevel.multipleResponses, jData));
        assertEquals(new LooseConnection(), com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(General.JFaultLevel.looseConnection, jData));
    }

    @Test
    public void testJToSJMockResult() {
        printSeparator("testJToSJMockResult");

        final byte[] data = {1, 2, 3, 4};

        final General.JFaultLevel level       = General.JFaultLevel.noFault;
        final JFaultLevelData   faultData   = new JFaultLevelData(0, 0);

        final JMockResult jResult = new JMockResult(data, level, faultData);

        final Tuple2<byte[], FaultLevel> sResult = com.github.pheymann.mockitjavaapi.core.ConvertUtil.javaToScala(jResult);

        assertEquals(data, sResult._1());
        assertEquals(new NoFault(), sResult._2());
    }

}
