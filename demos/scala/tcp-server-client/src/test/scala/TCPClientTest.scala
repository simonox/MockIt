import java.util.concurrent.{TimeUnit, Executors}
import scala.collection.mutable.ListBuffer

import org.junit.{After, Test, Before}

import org.mockit.MockIt
import org.mockit.annotation.{MockItConfig, MockItConfigs}
import org.mockit.core.{ShutdownLatch, ClientConfiguration, ConnectionType}

@MockItConfigs
class TCPClientTest {

    val pool = Executors.newSingleThreadExecutor

    var server: Thread = null

    @MockItConfig(mockKey = "test2", mockUnit = "Factor2ClientMock")
    val testConfig = new ClientConfiguration(
        targetPort      = ServerActionWorker.serverPort,
        repetitions     = 2,
        threadNumber    = 2,
        mockConnection  = ConnectionType.tcp
    )
    @MockItConfig(mockKey = "test2", mockUnit = "Factor3ClientMock")
    val testConfig2 = new ClientConfiguration(
        targetPort      = ServerActionWorker.serverPort,
        mockConnection  = ConnectionType.tcp
    )

    @Before def init: Unit = {
        server = ServerActionWorker.init(5)
    }

    @After def delete {
        pool.shutdown
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }

    @Test def testTCPClient: Unit = {
        val serverIps   = new ListBuffer[(String, String, Int)]
        val shutdown    = new ShutdownLatch
        val latch       = new ShutdownLatch

        pool.submit(MockIt.mockNetwork("test2", "", serverIps, shutdown, latch))

        latch.await
        server.join
        shutdown.close
    }

}
