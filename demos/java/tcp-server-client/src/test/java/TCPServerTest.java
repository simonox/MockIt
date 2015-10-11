import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockit.General;
import org.mockit.JMockIt;
import org.mockit.core.JMockCallable;
import org.mockit.core.JServerConfiguration;
import org.mockit.core.ShutdownLatch;
import org.mockit.annotation.MockItConfig;
import org.mockit.annotation.MockItConfigs;

@MockItConfigs
public class TCPServerTest {

    private final int               PORT        = 1234;

    private final ExecutorService   pool        = Executors.newSingleThreadExecutor();

    private JMockCallable           mockJob     = null;
    private List<Object[]>          serverIps   = null;

    private ShutdownLatch           shutdown    = new ShutdownLatch();

    @MockItConfig(mockKey = "test", mockUnit = "Multiply2ServerMock")
    private final JServerConfiguration testConfig = new JServerConfiguration(
        PORT,
        1,
        General.JConnectionType.tcp
    );
    @MockItConfig(mockKey = "test", mockUnit = "Multiply3ServerMock")
    private final JServerConfiguration testConfig2 = new JServerConfiguration(
        PORT + 1,
        1,
        General.JConnectionType.tcp
    );

    @Before
    public void init() throws Exception {
        final ShutdownLatch latch = new ShutdownLatch();

        shutdown    = new ShutdownLatch();
        mockJob     = JMockIt.mockNetwork("test", "", shutdown, latch);

        pool.submit(mockJob);
        latch.await();

        serverIps   = mockJob.getServerIps();
    }

    @After
    public void delete() throws Exception {
        serverIps.clear();

        shutdown.close();
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void testTCPServer() throws Exception {
        final int x = 4;
        final int y = 5;

        int factor  = 0;

        for (Object[] serverIp : serverIps) {
            final ClientAction client = new ClientAction((Integer) serverIp[2], (String) serverIp[1]);

            if (((String) serverIp[0]).contains("Multiply2"))
                factor = 2;
            else
                factor = 3;

            assertEquals(x * factor + y * factor, client.action(x, y));
            assertEquals(x * factor + y * factor, client.action(x, y));
        }
    }

}
