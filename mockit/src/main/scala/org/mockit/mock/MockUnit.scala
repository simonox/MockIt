package org.mockit.mock

/**
 * Basic mock pattern. Stores the class name
 * of the actual mock unit and defines the initialization
 * process.
 *
 * @author  pheymann
 * @version 0.1.0
 */
trait MockUnit extends Serializable {

    lazy val name = this.getClass.getSimpleName

    /**
     * Initialization of class attributes and
     * environment.
     */
    def init(): Unit

}
