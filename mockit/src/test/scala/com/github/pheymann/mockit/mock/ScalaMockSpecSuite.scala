package com.github.pheymann.mockit.mock

import com.github.pheymann.mockit.mock.http.{HttpClientMockSpec, HttpMockServerSpec}

import com.github.pheymann.mockit.mock.tcp.{TCPMockServerSpec, TCPMockWorkerSpec}
import com.github.pheymann.mockit.mock.udp.UDPMockWorkerSpec
import org.scalatest.Suites

class ScalaMockSpecSuite extends Suites(
    new UDPMockWorkerSpec,
    new TCPMockWorkerSpec,
    new TCPMockServerSpec,
    new HttpMockServerSpec,
    new HttpClientMockSpec
)
