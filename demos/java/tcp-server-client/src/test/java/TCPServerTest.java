import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.pheymann.mockit.core.Configuration;
import com.github.pheymann.mockitjavaapi.core.ConfigurationFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.github.pheymann.mockitjavaapi.General;
import com.github.pheymann.mockitjavaapi.JMockIt;
import com.github.pheymann.mockitjavaapi.core.JMockCallable;
import com.github.pheymann.mockit.core.ShutdownLatch;
import com.github.pheymann.mockit.annotation.MockItConfig;
import com.github.pheymann.mockit.annotation.MockItConfigs;

@MockItConfigs
public class TCPServerTest {

    private final int               PORT        = 1234;

    private final ExecutorService   pool        = Executors.newSingleThreadExecutor();

    private JMockCallable           mockJob     = null;
    private List<Object[]>          serverIps   = null;

    private ShutdownLatch           shutdown    = new ShutdownLatch();

    @MockItConfig(mockKey = "test", mockUnit = "Multiply2ServerMock")
    private final Configuration testConfig = ConfigurationFactory.createServer(
        PORT,
        1,
        General.JConnectionType.tcp
    );
    @MockItConfig(mockKey = "test", mockUnit = "Multiply3ServerMock")
    private final Configuration testConfig2 =ConfigurationFactory.createServer(
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
