package com.github.pheymann.mockitjavaapi.mock.tcp;

import scala.Tuple2;

import com.github.pheymann.mockit.mock.tcp.TCPMockUnit;
import com.github.pheymann.mockitjavaapi.core.ConvertUtil;
import com.github.pheymann.mockit.core.FaultLevel;
import com.github.pheymann.mockitjavaapi.core.JMockResult;

/**
 * This subclass is the java interface. It wraps the scala
 * functions including special signs into a java style
 * representation.
 *
 * @see com.github.pheymann.mockit.mock.tcp.TCPMockUnit
 *
 * @author  pheymann
 * @version 0.1.0
 */
public abstract class JTCPMockUnit extends TCPMockUnit {

    public abstract JMockResult jMock();

    @Override
    public Tuple2<byte[], FaultLevel> mock() {
        return ConvertUtil.javaToScala(jMock());
    }

}
