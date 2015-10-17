package com.github.pheymann.mockit.core

import com.github.pheymann.mockit.mock.MockUnit

/**
 * Collection of '''MockIt''' specific exceptions
 *
 * @author  pheymann
 * @version 0.1.0
 */

/**
 * Interface exception which wraps internal exceptions and
 * leads them to the developers application layer.
 *
 * @param e
 *          any exception which occurs internally
 */
case class MockItException(e: Throwable)            extends Exception(e)

/**
 * Is thrown if no [[com.github.pheymann.mockit.core.Configuration]] is found
 * for a [[com.github.pheymann.mockit.mock.MockUnit]].
 *
 * @param msg
 *          describes in detail which [[com.github.pheymann.mockit.mock.MockUnit]] misses
 *          his configuration
 */
case class NoMockUnitConfigException(msg: String)   extends Exception(msg)

/**
 * Is thrown if the [[com.github.pheymann.mockit.core.MockAgent]] can't connect to
 * the [[com.github.pheymann.mockit.network.UploadServer]] after receiving an
 * invitation.
 */
case class UploadServerDownException()              extends Exception
