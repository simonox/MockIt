package org.mockit.core

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
 * Is thrown if no [[org.mockit.core.Configuration]] is found
 * for a [[org.mockit.mock.MockUnit]].
 *
 * @param msg
 *          describes in detail which [[org.mockit.mock.MockUnit]] misses
 *          his configuration
 */
case class NoMockUnitConfigException(msg: String)   extends Exception(msg)

/**
 * Is thrown if the [[org.mockit.core.MockAgent]] can't connect to
 * the [[org.mockit.network.UploadServer]] after receiving an
 * invitation.
 */
case class UploadServerDownException()              extends Exception
