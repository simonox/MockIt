import java.util.concurrent.{TimeUnit, Executors}

import org.scalatest.{BeforeAndAfter, FlatSpec}

import scala.collection.mutable.ListBuffer

import com.github.pheymann.mockit.MockIt
import com.github.pheymann.mockit.annotation.{MockItConfig, MockItConfigs}
import com.github.pheymann.mockit.core.{ShutdownLatch, ClientConfiguration, ConnectionType}

@MockItConfigs
class TCPClientTest extends FlatSpec with BeforeAndAfter {

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

    before {
        server = ServerActionWorker.init(5)
    }

    after {
        pool.shutdown()
        pool.awaitTermination(1, TimeUnit.MINUTES)
    }

    "A TCPFactorClient" should "send numbers (factor) to a server" in {
        val serverIps   = new ListBuffer[(String, String, Int)]
        val shutdown    = new ShutdownLatch
        val latch       = new ShutdownLatch

        pool.submit(MockIt.mockNetwork("test2", "", serverIps, shutdown, latch))

        latch.await()
        server.join()
        shutdown.close
    }

}
