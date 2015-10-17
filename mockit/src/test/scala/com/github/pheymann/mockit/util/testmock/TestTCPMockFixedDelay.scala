package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.mock.tcp.TCPMockWorkerTest
import com.github.pheymann.mockit.core.FixedDelay
import com.github.pheymann.mockit.mock.tcp.TCPMockWorkerTest

class TestTCPMockFixedDelay extends BasicTCPTestMock {

    override def faultLevel = FixedDelay(TCPMockWorkerTest.delay)

}
