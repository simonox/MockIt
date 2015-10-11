package org.mockit.core

import java.util.concurrent.CountDownLatch

/**
 * Extension of the [[CountDownLatch]] with a counter
 * of one.
 *
 * Is used to broadcast a shutdown signal.
 *
 * @author  pheymann
 * @version 0.1.0
 */
class ShutdownLatch extends CountDownLatch(1) {

    def close: Unit = countDown

    def continue: Boolean = getCount > 0

}
