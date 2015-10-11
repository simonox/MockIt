package org.mockit.util.testmock

import org.mockit.core.MultipleResponses
import org.mockit.mock.tcp.TCPMockWorkerTest

class TestTCPMockMultipleResponses extends BasicTCPTestMock {

    override def faultLevel = MultipleResponses(TCPMockWorkerTest.factor)

}
