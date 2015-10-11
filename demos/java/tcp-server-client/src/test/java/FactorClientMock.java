import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.mockit.General;
import org.mockit.core.JMockResult;
import org.mockit.mock.tcp.TCPMockUnitConvert;

public abstract class FactorClientMock extends TCPMockUnitConvert {

    private static final int maxCalculations    = 2;

    private boolean isResponse                  = false;
    private int     calculationCount            = 0;
    private int     receivedData                = 0;

    public abstract int factor();

    @Override
    public void init() {}

    @Override
    public boolean isNext() {
        return calculationCount < maxCalculations;
    }

    @Override
    public boolean waitOnData() {
        return isResponse;
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

        objOut.writeInt(factor());
        objOut.flush();

        ++calculationCount;
        isResponse = true;

        return new JMockResult(bOut.toByteArray(), General.JFaultLevel.noFault, null);
    }

}
