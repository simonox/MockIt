package com.github.pheymann.mockitjavaapi.util;

import java.io.InputStream;

import com.github.pheymann.mockit.annotation.MockIt;
import com.github.pheymann.mockitjavaapi.General;
import com.github.pheymann.mockitjavaapi.core.JMockResult;
import com.github.pheymann.mockitjavaapi.mock.tcp.JTCPMockUnit;

/**
 * @author  pheymann
 * @version 0.1.0
 */
@MockIt(mockKeys = {"test-key"})
public class JTestTCPMock extends JTCPMockUnit {

    private final byte[]        result      = {1, 2, 3, 4};
    private final JMockResult jResponse     = new JMockResult(result, General.JFaultLevel.noFault, null);

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
