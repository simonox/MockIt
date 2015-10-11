package org.mockit.logging

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(classOf[Suite])
@SuiteClasses(
    Array(
        classOf[LogChannelTest],
        classOf[NetworkLoggerTest]
    )
)
class ScalaLoggingTestSuite
