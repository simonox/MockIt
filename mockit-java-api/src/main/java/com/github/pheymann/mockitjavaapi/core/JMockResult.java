package com.github.pheymann.mockitjavaapi.core;

import com.github.pheymann.mockitjavaapi.General;

/**
 * Java container for {@link com.github.pheymann.mockit.mock.ProtocolMockUnit} mock result.
 *
 * During execution it is cast into a scala tuple.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JMockResult {

    private final byte[]            response;

    private final General.JFaultLevel fault;
    private final JFaultLevelData   data;



    public JMockResult(
                        final byte[]            response,
                        final General.JFaultLevel fault,
                        final JFaultLevelData   data
                      ) {
        this.response = response;

        this.fault = fault;
        this.data = data;
    }

    public final byte[] getResponse() {
        return response;
    }

    public final General.JFaultLevel getFault() {
        return fault;
    }

    public final JFaultLevelData getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal;

        if (obj == null || !(obj instanceof JMockResult)) {
            equal = false;
        }
        else {
            final JMockResult result = (JMockResult) obj;

            equal = (
                this.fault.equals(result.fault) &&
                this.response.equals(result.getResponse())
            );

            if (this.data != null){
                equal = this.data.equals(result.getData());
            }
        }
        return equal;
    }

}
