package com.github.pheymann.mockit.mock

import com.github.pheymann.mockit.mock.http.{HttpClientMockTest, HttpMockServerTest}

import com.github.pheymann.mockit.mock.tcp.{TCPMockServerTest, TCPMockWorkerTest}
import com.github.pheymann.mockit.mock.udp.UDPMockWorkerTest
import org.scalatest.Suites

class ScalaMockSpecSuite extends Suites(
    new UDPMockWorkerTest,
    new TCPMockWorkerTest,
    new TCPMockServerTest,
    new HttpMockServerTest,
    new HttpClientMockTest
)
