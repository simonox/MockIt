package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.mock.tcp.TCPMockWorkerSpec
import com.github.pheymann.mockit.core.FixedDelay

class TestTCPMockFixedDelay extends BasicTCPTestMock {

    override def faultLevel = FixedDelay(TCPMockWorkerSpec.delay)

}
