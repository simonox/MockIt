package org.mockit.core

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(classOf[Suite])
@SuiteClasses(
    Array(
        classOf[ConvertUtilTest],
        classOf[MockAgentTest],
        classOf[MockFactoryTest]
    )
)
class ScalaCoreTestSuite
