import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mockit.General;
import org.mockit.JMockIt;
import org.mockit.core.JClientConfiguration;
import org.mockit.core.ShutdownLatch;
import org.mockit.annotation.MockItConfig;
import org.mockit.annotation.MockItConfigs;

@MockItConfigs
public class TCPClientTest {

    final ExecutorService   pool = Executors.newSingleThreadExecutor();

    Thread                  server = null;

    @MockItConfig(mockKey = "test2", mockUnit = "Factor2ClientMock")
    JClientConfiguration testConfig = new JClientConfiguration(
        2,
        2,
        ServerActionWorker.SERVER_PORT,
        General.DEF_IP,
        1,
        General.JConnectionType.tcp
    );
    @MockItConfig(mockKey = "test2", mockUnit = "Factor3ClientMock")
    JClientConfiguration testConfig2 = new JClientConfiguration(
        1,
        1,
        ServerActionWorker.SERVER_PORT,
        General.DEF_IP,
        1,
        General.JConnectionType.tcp
    );

    @Before
    public void init() throws Exception {
        server = ServerActionWorker.init(5);
    }

    @After
    public void delete() throws Exception {
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void testTCPClient() throws Exception {
        final ShutdownLatch shutdown    = new ShutdownLatch();
        final ShutdownLatch latch       = new ShutdownLatch();

        pool.submit(JMockIt.mockNetwork("test2", "", shutdown, latch));

        latch.await();
        server.join();
        shutdown.close();
    }

}
