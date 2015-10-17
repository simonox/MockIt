package com.github.pheymann.mockittool

import java.util.ResourceBundle

object MessageProvider {

    private val bundle = ResourceBundle.getBundle(MSG_BUNDLE)

    /**
     * Returns the message which corresponds to
     * the given key or the key of no message is found.
     *
     * @param key
     * @return message
     *         is key if no message is found
     */
    def msg(key: String): String = {
        var value: String = key

        try {
            value = bundle.getString(key)
        }
        catch {
            case e: Throwable =>
        }
        value
    }

}