package com.github.pheymann.mockit.util

object TestUtil {

    def checkArrayEquals(expected: Array[Byte], actual: Array[Byte]): Boolean = {
        if (expected.length != actual.length)
            false
        else {
            var equal = true

            for (i <- 0 until expected.length)
                equal &&= expected(i) == actual(i)
            equal
        }
    }

}
