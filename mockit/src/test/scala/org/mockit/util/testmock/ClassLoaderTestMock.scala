package org.mockit.util.testmock

import org.mockit.annotation.MockIt

@MockIt(mockKeys = Array("dr-test"))
class ClassLoaderTestMock extends TestTCPMock {

    val subField = 5

}
