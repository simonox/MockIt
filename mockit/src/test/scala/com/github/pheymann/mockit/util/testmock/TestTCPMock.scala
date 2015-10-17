package com.github.pheymann.mockit.util.testmock

import java.io.InputStream

import com.github.pheymann.mockit.core.{FaultLevel, NoFault}
import com.github.pheymann.mockit.mock.tcp.TCPMockUnit

object TestTCPMock {

    val testResponse: Array[Byte] = Array[Byte](1, 2, 3, 4)

}

class TestTCPMock extends TCPMockUnit {

    val faultLevel  = NoFault()
    val response    = TestTCPMock.testResponse

    var counter: Int = 0

    override def init: Unit = {}

    override def receiveData(inStream: InputStream): Unit = {}

    override def isNext: Boolean = {
        counter += 1
        counter <= 1
    }

    override def waitOnData: Boolean = false

    override def mock: (Array[Byte], FaultLevel) = (response, faultLevel)

}
