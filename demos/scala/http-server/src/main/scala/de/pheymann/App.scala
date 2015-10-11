package de.pheymann

import org.mockit.MockIt
import org.mockit.core.ShutdownLatch

object App {

    def main(args: Array[String]): Unit = {
        val shutdown = new ShutdownLatch
        val latch = new ShutdownLatch

        MockIt.mockLocal("test-key", "", shutdown, latch).call
    }

}
