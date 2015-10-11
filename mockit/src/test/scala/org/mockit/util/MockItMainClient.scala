package org.mockit.util

import java.net.SocketTimeoutException

import org.mockit.core.{UploadServerDownException, MockFactory}
import org.mockit.logging.Logger
import org.mockit.network.ServerListener

class MockItMainClient(
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
