import java.util.concurrent.{Executors, TimeUnit}

import org.scalatest.{Matchers, FlatSpec, BeforeAndAfter}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.MockIt
import com.github.pheymann.mockit.core.{ConnectionType, ServerConfiguration, ShutdownLatch}
import com.github.pheymann.mockit.annotation.{MockItConfig, MockItConfigs}

@MockItConfigs
class TCPServerTest extends FlatSpec with BeforeAndAfter with Matchers {

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

    before {
        val latch = new ShutdownLatch

        shutdown = new ShutdownLatch

        pool.submit(MockIt.mockNetwork("test", "", serverIps, shutdown,latch))
        latch.await()
    }

    after {
        serverIps.clear()

        shutdown.close
        pool.shutdown()
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }

    "A MultiplyServer" should "multiply a number provided by a client" in {
        val x = 4
        val y = 5

        var factor  = 0

        for ((mock, ip, port) <- serverIps) {
            val client = new ClientAction(ip, port)

            if (mock.contains("Multiply2"))
                factor = 2
            else
                factor = 3

            (x * factor + y * factor) should be (client.action(x, y))
            (x * factor + y * factor) should be (client.action(x, y))
        }
    }

}
