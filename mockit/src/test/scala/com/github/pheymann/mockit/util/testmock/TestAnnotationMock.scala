package com.github.pheymann.mockit.util.testmock

import java.io.InputStream

import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.core.FaultLevel
import com.github.pheymann.mockit.mock.tcp.TCPMockUnit
import com.github.pheymann.mockit.annotation.{MockItConfigs, MockItConfig, MockIt}

@MockItConfigs
class TestConfig {

    @MockItConfig(mockUnit = "TestAnnotationMock", mockKey = "annotation-test")
    val config = new ServerConfiguration(1, 1, ConnectionType.tcp)

    @MockItConfig(mockUnit = "ClassLoaderTestMock", mockKey = "dr-test")
    val config2 = new ServerConfiguration(1, 1, ConnectionType.tcp)

}

@MockIt(mockKeys = Array("annotation-test"))
class TestAnnotationMock extends TCPMockUnit {

    override def init(): Unit = {}

    override def waitOnData: Boolean = true

    override def receiveData(inStream: InputStream): Unit = {}

    override def isNext: Boolean = true

    override def mock: (Array[Byte], FaultLevel) = null

}
