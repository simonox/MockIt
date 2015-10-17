package com.github.pheymann.mockit.core

import org.scalatest.Suites

class ScalaCoreSpecSuite extends Suites(
    new MockAgentSpec,
    new MockFactorySpec
)
