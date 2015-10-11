package org.mockit.network

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(classOf[Suite])
@SuiteClasses(
    Array(
        classOf[UploadServerTest],
        classOf[LogServerTest],
        classOf[SetUptProtocolTest],
        classOf[NetworkTest]
    )
)
class ScalaNetworkTestSuite
