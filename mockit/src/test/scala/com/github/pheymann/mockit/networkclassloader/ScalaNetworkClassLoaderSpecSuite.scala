package com.github.pheymann.mockit.networkclassloader

import com.github.pheymann.mockit.core.DependencyResolverSpec
import org.scalatest.Suites

class ScalaNetworkClassLoaderSpecSuite extends Suites(
    new ClassStreamSpec,
    new NetworkClassLoaderSpec,
    new DependencyResolverSpec
)
