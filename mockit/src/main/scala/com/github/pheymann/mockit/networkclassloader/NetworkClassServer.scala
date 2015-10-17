package com.github.pheymann.mockit.networkclassloader

import java.net.Socket
import com.github.pheymann.mockit.core.MockUnitContainer
import com.github.pheymann.mockit.logging.Logger

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
