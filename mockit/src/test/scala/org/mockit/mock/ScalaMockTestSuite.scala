package org.mockit.mock

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.mockit.mock.http.{HttpClientMockTest, HttpMockServerTest}

import org.mockit.mock.tcp.{TCPMockServerTest, TCPMockWorkerTest}
import org.mockit.mock.udp.UDPMockWorkerTest

@RunWith(classOf[Suite])
@SuiteClasses(
    Array(
        classOf[UDPMockWorkerTest],
        classOf[TCPMockWorkerTest],
        classOf[TCPMockServerTest],
        classOf[HttpMockServerTest],
        classOf[HttpClientMockTest]
    )
)
class ScalaMockTestSuite
