package com.github.pheymann.mockitjavaapi;

/**
 * Basic test class which provides common functions
 * and procedures to support the test case implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class MockItBasicTest {

    private static final String SEPARATOR_LINE = "--------------------- %s.%s\n";

    private String testName;

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void printSeparator(final String test) {
        System.out.printf(SEPARATOR_LINE, testName, test);
    }

}