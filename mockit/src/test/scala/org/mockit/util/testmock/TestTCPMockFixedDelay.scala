package org.mockit.util.testmock

import org.mockit.core.FixedDelay
import org.mockit.mock.tcp.TCPMockWorkerTest

class TestTCPMockFixedDelay extends BasicTCPTestMock {

    override def faultLevel = FixedDelay(TCPMockWorkerTest.delay)

}
