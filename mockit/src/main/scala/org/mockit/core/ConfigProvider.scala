package org.mockit.core

import java.util.ResourceBundle

/**
 * Loads '''MockIt''' configurations.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object ConfigProvider {

    val bundle = ResourceBundle.getBundle(CONFIGS)

    /**
     * Returns configuration value for given key or the
     * key itself if no configuration is found.
     *
     * @param key
     * @return value
     *         is key if no value exists
     */
    def config(key: String): String = {
        var value = key

        try {
            value = bundle.getString(key)
        }
        catch {
            case e: Throwable =>
        }
        value
    }

}
