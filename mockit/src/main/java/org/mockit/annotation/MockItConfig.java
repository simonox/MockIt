package org.mockit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields as {@link org.mockit.core.Configuration} for
 * a specific {@link org.mockit.mock.MockUnit} and mock up.
 *
 * @author  pheymann
 * @version 0.1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockItConfig {

    String mockUnit();
    String mockKey() default "";

}
