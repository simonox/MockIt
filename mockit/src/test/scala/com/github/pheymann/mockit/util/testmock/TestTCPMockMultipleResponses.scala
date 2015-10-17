package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.core.MultipleResponses
import com.github.pheymann.mockit.mock.tcp.TCPMockWorkerTest

class TestTCPMockMultipleResponses extends BasicTCPTestMock {

    override def faultLevel = MultipleResponses(TCPMockWorkerTest.factor)

}
