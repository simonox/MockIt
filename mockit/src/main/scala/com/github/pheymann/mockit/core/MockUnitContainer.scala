package com.github.pheymann.mockit.core

/**
 * Container instance to carry a [[com.github.pheymann.mockit.mock.MockUnit]]
 * and his [[com.github.pheymann.mockit.core.Configuration]].
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
