package org.mockit.core

/**
 * Container instance to carry a [[org.mockit.mock.MockUnit]]
 * and his [[org.mockit.core.Configuration]].
 *
 * @author  pheymann
 * @version 0.1.0
 *
 * @param mockName
 *              name of this mock unit; can be the class name of something else,
 *              but has to be unique
 * @param mockClass
 *              ''ByteCode'' of the mock unit
 * @param config
 *              configuration
 */
case class MockUnitContainer(
                                mockName:   String,
                                mockClass:  Array[Byte],
                                config:     Configuration
                            )
