package com.github.pheymann.mockit.logging

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

import com.github.pheymann.mockit.MockItSpec
import com.github.pheymann.mockit.core.core._

/**
 * @author  pheymann
 * @version 0.1.0
 */
class LogChannelSpec extends MockItSpec {

    import NetworkLogger.LogLevel._

    "A LogChannel " should "send all stored messages to the defined origin" in {
        val channel = new LogChannel

        val time = System.currentTimeMillis
        val msg = "test message"
        val data = new LogData(time)
        val entry = new LogEntry(error, data, None, msg)

        channel.log(error, data, None, msg)

        val server = new ServerSocket(DEFAULT_PORT)
        val clientWorker = new Runnable {
            override def run(): Unit = {
                val client = new Socket(DEFAULT_IP, DEFAULT_PORT)

                channel.send(new ObjectOutputStream(client.getOutputStream))
                client.close()
            }
        }
        new Thread(clientWorker).start()

        val client = server.accept
        val inStream = new ObjectInputStream(client.getInputStream)

        inStream.readInt()  should be (1)
        channel.size        should be (1)

        val externalEntry = inStream.readObject

        externalEntry       should be (entry)

        server.close()
    }

}