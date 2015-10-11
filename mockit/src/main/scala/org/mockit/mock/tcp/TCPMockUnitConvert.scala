package org.mockit.mock.tcp

import org.mockit.core.{FaultLevel, JMockResult}

/**
 * This subclass is the java interface. It wraps the scala
 * functions including special signs into a java style
 * representation.
 *
 * @see [[TCPMockUnit]]
 *
 * @author  pheymann
 * @version 0.1.0
 */
abstract class TCPMockUnitConvert extends TCPMockUnit {

    import org.mockit.core.ConvertUtil._

    @throws(classOf[Exception])
    def jMock: JMockResult

    override def mock: (Array[Byte], FaultLevel) = {
        jMock
    }

}
