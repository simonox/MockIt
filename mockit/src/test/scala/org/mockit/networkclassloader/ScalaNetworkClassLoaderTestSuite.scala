package org.mockit.networkclassloader

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.mockit.core.DependencyResolverTest

@RunWith(classOf[Suite])
@SuiteClasses(
    Array(
        classOf[ClassStreamTest],
        classOf[NetworkClassLoadTest],
        classOf[DependencyResolverTest]
    )
)
class ScalaNetworkClassLoaderTestSuite
