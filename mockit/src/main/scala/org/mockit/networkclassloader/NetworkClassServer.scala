package org.mockit.networkclassloader

import java.net.Socket

import org.mockit.core.MockUnitContainer
import org.mockit.logging.Logger

trait NetworkClassServer extends Logger {

    def sendClasses(
                       mockUnit:        MockUnitContainer,
                       mockComponents:  Map[String, Array[Byte]],
                       client:          Socket
                   ): Unit = {
        >> ("send %d component classes".format(mockComponents.size))
        ClassOutputStream.send(mockComponents, client, this)

        >> ("send mock unit %s".format(mockUnit.mockName))
        ClassOutputStream.send(mockUnit.mockClass, mockUnit.mockName, client)
    }

}
