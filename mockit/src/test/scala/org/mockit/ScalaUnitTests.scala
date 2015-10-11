package org.mockit

import org.junit.runner.RunWith
import org.junit.runners.Suite

import org.mockit.core._
import org.mockit.logging.ScalaLoggingTestSuite
import org.mockit.mock.ScalaMockTestSuite
import org.mockit.network.ScalaNetworkTestSuite
import org.mockit.networkclassloader.ScalaNetworkClassLoaderTestSuite

@RunWith(classOf[Suite])
@Suite.SuiteClasses(
    Array(
        classOf[ScalaCoreTestSuite],
        classOf[ScalaMockTestSuite],
        classOf[ScalaLoggingTestSuite],
        classOf[ScalaNetworkTestSuite],
        classOf[ScalaNetworkClassLoaderTestSuite]
    )
)
class ScalaUnitTests