package com.github.pheymann.mockitjavaapi.core;

import com.github.pheymann.mockit.core.Configuration;
import com.github.pheymann.mockit.core.MockUnitContainer;
import com.github.pheymann.mockit.mock.MockUnit;
import com.github.pheymann.mockit.networkclassloader.ClassConverter$;

/**
 * Created by pheymann on 15.10.15.
 */
public class MockUnitContainerFactory {

    public static MockUnitContainer create(
        final String                    mockName,
        final Class<? extends MockUnit> mockClass,
        final Configuration config
    ) {
        return new MockUnitContainer(
            mockName,
            ClassConverter$.MODULE$.toByteArray(mockClass),
            config
        );
    }

}
