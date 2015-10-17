package com.github.pheymann.mockitjavaapi.core;

/**
 * Java wrapper for {@link com.github.pheymann.mockit.core.FaultLevel}. For a detailed
 * description use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JFaultLevelData {

    private final int   factor;
    private final long  time;

    public JFaultLevelData(final int factor, final int time) {
        this.factor = factor;
        this.time   = time;
    }

    public final int getFactor() {
        return factor;
    }

    public final long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal;

        if (obj == null || !(obj instanceof JFaultLevelData)) {
            equal = false;
        }
        else {
            final JFaultLevelData data = (JFaultLevelData) obj;

            equal = (
                this.factor == data.getFactor() &&
                this.time   == data.getTime()
            );
        }
        return equal;
    }

}
