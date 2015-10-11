package org.mockit.util.testmock

import org.mockit.core.LooseConnection

class TestTCPMockLooseConnection extends BasicTCPTestMock {

    override def faultLevel = LooseConnection()

}
