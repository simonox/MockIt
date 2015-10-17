package com.github.pheymann.mockit

import org.scalatest.Suites

import com.github.pheymann.mockit.core._
import com.github.pheymann.mockit.logging.ScalaLoggingSpecSuite
import com.github.pheymann.mockit.mock.ScalaMockSpecSuite
import com.github.pheymann.mockit.network.ScalaNetworkSpecSuite
import com.github.pheymann.mockit.networkclassloader.ScalaNetworkClassLoaderSpecSuite

class MockItSpecSuite extends Suites(
    new ScalaCoreSpecSuite,
    new ScalaMockSpecSuite,
    new ScalaLoggingSpecSuite,
    new ScalaNetworkSpecSuite,
    new ScalaNetworkClassLoaderSpecSuite
)