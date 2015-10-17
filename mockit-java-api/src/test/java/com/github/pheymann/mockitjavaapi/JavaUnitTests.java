package com.github.pheymann.mockitjavaapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.pheymann.mockitjavaapi.core.ConvertUtilTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ConvertUtilTest.class,
    JMockItTest.class
})
public class JavaUnitTests {
}
