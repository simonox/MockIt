package com.github.pheymann.mockit.util.testmock

import java.net.DatagramPacket

import com.github.pheymann.mockit.core.{FaultLevel, NoFault}
import com.github.pheymann.mockit.mock.udp.UDPMockUnit

class TestUDPMock extends UDPMockUnit {

    var receiveData = new Array[Byte](TestTCPMock.testResponse.length)

    val faultLevel  = NoFault()
    val response    = TestTCPMock.testResponse

    var counter:        Int             = 0

    override def init: Unit = {
        receivePackage = new DatagramPacket(receiveData, 0, receiveData.length)
    }

    override def isNext: Boolean = {
        counter += 1
        counter <= 1
    }

    override def waitOnData: Boolean = counter <= 1

    override def mock: (Array[Byte], FaultLevel) = (response, faultLevel)

}
