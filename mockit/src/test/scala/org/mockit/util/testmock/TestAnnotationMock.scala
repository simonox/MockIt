package org.mockit.util.testmock

import java.io.InputStream

import org.mockit.core._
import org.mockit.annotation.{MockItConfig, MockItConfigs, MockIt}
import org.mockit.core.FaultLevel
import org.mockit.mock.tcp.TCPMockUnit

@MockItConfigs
class TestConfig {

    @MockItConfig(mockUnit = "TestAnnotationMock", mockKey = "annotation-test")
    val config = new ServerConfiguration(1, 1, ConnectionType.tcp)

    @MockItConfig(mockUnit = "ClassLoaderTestMock", mockKey = "dr-test")
    val config2 = new ServerConfiguration(1, 1, ConnectionType.tcp)

}

@MockIt(mockKeys = Array("annotation-test"))
class TestAnnotationMock extends TCPMockUnit {

    override def init: Unit = {}

    override def waitOnData: Boolean = true

    override def receiveData(inStream: InputStream): Unit = {}

    override def isNext: Boolean = true

    override def mock: (Array[Byte], FaultLevel) = null

}
