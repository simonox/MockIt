package org.mockit.util;

import org.mockit.General;
import org.mockit.core.JMockResult;
import org.mockit.mock.tcp.TCPMockUnitConvert;

import java.io.InputStream;

public class JTestTCPMock extends TCPMockUnitConvert {

    private final byte[]        result      = {1, 2, 3, 4};
    private final JMockResult jResponse   = new JMockResult(result, General.JFaultLevel.noFault, null);

    private int                 counter     = 0;

    @Override
    public void init() {}

    @Override
    public boolean isNext() {
        return (++counter) <= 1;
    }

    @Override
    public boolean waitOnData() {
        return false;
    }

    @Override
    public void receiveData(InputStream inStream) {}

    @Override
    public JMockResult jMock() {
        return jResponse;
    }

}
