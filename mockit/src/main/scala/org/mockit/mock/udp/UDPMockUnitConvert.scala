package org.mockit.mock.udp

import org.mockit.core.{ConvertUtil, FaultLevel, JMockResult}

/**
 * This subclass is the java interface. It wraps the scala
 * functions including special signs into a java style
 * representation.
 *
 * @see [[UDPMockUnit]]
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class UDPMockUnitConvert extends UDPMockUnit {

    import ConvertUtil._

    def jMock: JMockResult

    override def mock: (Array[Byte], FaultLevel) = {
        jMock
    }
    
}
