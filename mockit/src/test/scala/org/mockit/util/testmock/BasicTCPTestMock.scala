package org.mockit.util.testmock

import java.io.InputStream

import org.mockit.core.FaultLevel
import org.mockit.mock.tcp.TCPMockUnit

abstract class BasicTCPTestMock extends TCPMockUnit {

    var counter: Int = 0

    def faultLevel: FaultLevel

    override def init: Unit = {}

    override def isNext: Boolean = {
        counter += 1
        counter <= 1
    }

    override def waitOnData: Boolean = false

    override def receiveData(inStream: InputStream): Unit = {}

    override def mock: (Array[Byte], FaultLevel) = (TestTCPMock.testResponse, faultLevel)

}
