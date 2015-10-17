package com.github.pheymann.mockitjavaapi.mock.udp;

import scala.Tuple2;

import com.github.pheymann.mockit.mock.udp.UDPMockUnit;
import com.github.pheymann.mockitjavaapi.core.JMockResult;
import com.github.pheymann.mockitjavaapi.core.ConvertUtil;
import com.github.pheymann.mockit.core.FaultLevel;

/**
 * This subclass is the java interface. It wraps the scala
 * functions including special signs into a java style
 * representation.
 *
 * @see com.github.pheymann.mockit.mock.udp.UDPMockUnit
 *
 * @author  pheymann
 * @version 0.1.0
 */
public abstract class JUDPMockUnit extends UDPMockUnit {

    public abstract JMockResult jMock();

    @Override
    public Tuple2<byte[], FaultLevel> mock() {
        return ConvertUtil.javaToScala(jMock());
    }

}
