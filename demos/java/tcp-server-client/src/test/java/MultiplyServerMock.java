import java.io.*;

import org.mockit.General;
import org.mockit.core.JMockResult;
import org.mockit.mock.tcp.TCPMockUnitConvert;

public abstract class MultiplyServerMock extends TCPMockUnitConvert {

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
    public JMockResult jMock() throws Exception {
        final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        final ObjectOutputStream objOut = new ObjectOutputStream(bOut);

        objOut.writeInt(receivedData * multiplyFactor());
        objOut.flush();

        ++calculationCount;
        return new JMockResult(bOut.toByteArray(), General.JFaultLevel.noFault, null);
    }

}
