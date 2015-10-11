package org.mockit.util

import org.mockit.annotation.MockItComponent

@MockItComponent
class TestClass extends TestClassInterface
                with    Serializable {

    var param0: Int = 1
    var param1: Int = 2

    def test(): Int = {
        param0 + param1
    }

}
