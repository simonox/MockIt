import java.io.*;

import com.github.pheymann.mockitjavaapi.General;
import com.github.pheymann.mockitjavaapi.core.JMockResult;
import com.github.pheymann.mockitjavaapi.mock.tcp.JTCPMockUnit;

public abstract class MultiplyServerMock extends JTCPMockUnit {

    private static final int maxCalculations    = 2;

    private int calculationCount                = 0;
    private int receivedData                    = 0;

    public abstract int multiplyFactor();

    @Override
    public void init() {}

    @Override
    public boolean isNext() {
        return calculationCount < maxCalculations;
    }

    @Override
    public boolean waitOnData() {
        return true;
    }

    @Override
    public void receiveData(final InputStream inStream) throws Exception {
        final ObjectInputStream objIn = new ObjectInputStream(inStream);

        receivedData = objIn.readInt();
    }

    @Override
    public JMockResult jMock() {
        JMockResult result = null;

        try {
            final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            final ObjectOutputStream objOut = new ObjectOutputStream(bOut);

            objOut.writeInt(receivedData * multiplyFactor());
            objOut.flush();

            ++calculationCount;
            result = new JMockResult(bOut.toByteArray(), General.JFaultLevel.noFault, null);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

}
