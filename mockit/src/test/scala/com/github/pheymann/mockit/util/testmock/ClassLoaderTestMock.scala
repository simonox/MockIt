package com.github.pheymann.mockit.util.testmock

import com.github.pheymann.mockit.annotation.MockIt

@MockIt(mockKeys = Array("dr-test"))
class ClassLoaderTestMock extends TestTCPMock {

    val subField = 5

}
