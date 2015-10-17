package com.github.pheymann.mockit.util

import java.net.SocketTimeoutException

import com.github.pheymann.mockit.core.{UploadServerDownException, MockFactory}
import com.github.pheymann.mockit.core.{UploadServerDownException, MockFactory}
import com.github.pheymann.mockit.logging.Logger
import com.github.pheymann.mockit.network.ServerListener

class MockItDaemonDummy(
                            val repetitions: Int
                       ) extends Runnable
                        with    Logger {

    override def run(): Unit = {
        for (i <- 0 until repetitions) {
            try {
                val listener = new ServerListener

                val address = listener.waitForInvitation
                val (config, mockClass) = listener.load(address)

                MockFactory.networkClient(config, mockClass)
            }
            catch {
                case e @ (_: SocketTimeoutException | _: UploadServerDownException) =>
                case e: Throwable => this error ("exception during MockIt test run", e)
            }
        }
    }

}
