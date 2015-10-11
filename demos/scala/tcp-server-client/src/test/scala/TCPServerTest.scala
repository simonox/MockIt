import java.util.concurrent.{Executors, TimeUnit}

import org.junit.Assert._
import org.junit.{After, Before, Test}

import org.mockit.MockIt
import org.mockit.annotation.{MockItConfig, MockItConfigs}
import org.mockit.core.{ConnectionType, ServerConfiguration, ShutdownLatch}

import scala.collection.mutable.ListBuffer

@MockItConfigs
class TCPServerTest {

    final val PORT  = 1234

    val serverIps   = new ListBuffer[(String, String, Int)]
    val pool        = Executors.newSingleThreadExecutor

    var shutdown: ShutdownLatch = null

    @MockItConfig(mockKey = "test", mockUnit = "Multiply2ServerMock")
    val testConfig = new ServerConfiguration(
        PORT,
        1,
        ConnectionType.tcp
    )
    @MockItConfig(mockKey = "test", mockUnit = "Multiply3ServerMock")
    val testConfig2 = new ServerConfiguration(
        PORT + 1,
        1,
        ConnectionType.tcp
    )

    @Before def init: Unit = {
        val latch = new ShutdownLatch

        shutdown = new ShutdownLatch

        pool.submit(MockIt.mockNetwork("test", "", serverIps, shutdown,latch))
        latch.await
    }

    @After def delete: Unit = {
        serverIps.clear

        shutdown.close
        pool.shutdown
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }

    @Test def testTCPServer: Unit = {
        val x = 4
        val y = 5

        var factor  = 0

        for ((mock, ip, port) <- serverIps) {
            val client = new ClientAction(ip, port)

            if (mock.contains("Multiply2"))
                factor = 2
            else
                factor = 3

            assertEquals(x * factor + y * factor, client.action(x, y))
            assertEquals(x * factor + y * factor, client.action(x, y))
        }
    }

}
