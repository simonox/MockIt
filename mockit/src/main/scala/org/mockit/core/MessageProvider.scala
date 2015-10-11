package org.mockit.core

import java.util.ResourceBundle

/**
 * Loads message elements for the [[org.mockit.MockItTool]].
 *
 * @author  pheymann
 * @version 0.1.0
 * @see org.mockit.MockItTool
 */
object MessageProvider {

    val bundle = ResourceBundle.getBundle(MSG_BUNDLE)

    /**
     * Returns the message which corresponds to
     * the given key or the key of no message is found.
     *
     * @param key
     * @return message
     *         is key if no message is found
     */
    def msg(key: String): String = {
        var value = key

        try {
            value = bundle.getString(key)
        }
        catch {
            case e: Throwable => {}
        }
        value
    }

}
