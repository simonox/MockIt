package org.mockit

/**
 * Basic test class which provides common functions
 * and procedures to support the test case implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class MockItBasicTest {

    final val SEPARATOR_LINE = "--------------------- %s.%s"

    lazy val testName = this.getClass.getSimpleName

    def --(test: String): Unit = println(SEPARATOR_LINE.format(testName, test))

    def printSeparator(test: String): Unit = -- (test)
}
