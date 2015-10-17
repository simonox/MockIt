package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.core.LooseConnection

class TestTCPMockLooseConnection extends BasicTCPTestMock {

    override def faultLevel = LooseConnection()

}
