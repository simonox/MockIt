package com.github.pheymann.mockit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes as component classes which
 * are necessary to run the {@link com.github.pheymann.mockit.mock.MockUnit}s.
 *
 * @author  pheymann
 * @version 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockItComponent {}
