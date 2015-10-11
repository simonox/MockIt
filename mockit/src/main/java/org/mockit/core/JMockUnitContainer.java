package org.mockit.core;

import org.mockit.mock.MockUnit;

/**
 * Java wrapper for {@link org.mockit.core.MockUnitContainer}. For a detailed
 * description use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JMockUnitContainer {

    private final String                    mockName;

    private final Class<? extends MockUnit> mockClass;

    private final JConfiguration            config;

    public JMockUnitContainer(
                                final String                    mockName,
                                final Class<? extends MockUnit> mockClass,
                                final JConfiguration            config) {
        this.mockName = mockName;

        this.mockClass = mockClass;

        this.config = config;
    }

    public final String getMockName() {
        return mockName;
    }

    public final Class<? extends MockUnit> getMockClass() {
        return mockClass;
    }

    public final JConfiguration getConfig() {
        return config;
    }

}
