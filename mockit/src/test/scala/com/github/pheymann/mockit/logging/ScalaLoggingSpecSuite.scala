package com.github.pheymann.mockit.logging

import org.scalatest.Suites

class ScalaLoggingSpecSuite extends Suites(
    new LogChannelSpec,
    new NetworkLoggerSpec
)
