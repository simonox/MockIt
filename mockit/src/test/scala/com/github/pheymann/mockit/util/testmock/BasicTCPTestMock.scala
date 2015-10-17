package com.github.pheymann.mockit.util.testmock

import java.io.InputStream

import com.github.pheymann.mockit.core.FaultLevel
import com.github.pheymann.mockit.core.FaultLevel
import com.github.pheymann.mockit.mock.tcp.TCPMockUnit

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
