package com.github.pheymann.mockit.network

import org.scalatest.Suites

class ScalaNetworkSpecSuite extends Suites(
    new UploadServerSpec,
    new LogServerSpec,
    new ServerIntegrationSpec,
    new MockFactoryNetworkSpec
)
