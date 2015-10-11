package org.mockit.core

import scala.collection.mutable.ListBuffer

import org.mockit.core.BasicMockType.BasicMockType
import org.mockit.core.ConnectionType.ConnectionType
import org.mockit.General.{JFaultLevel, JConnectionType, JBasicMockType}
import org.mockit.mock.http.{HttpResponse, HttpRequest}

/**
 * Collection of implicit functions for java to scala conversion.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object ConvertUtil {

    import org.mockit.networkclassloader.ClassConverter._
    
    implicit def javaToScala(jBasicType: JBasicMockType): BasicMockType = {
        jBasicType match {
            case JBasicMockType.client => BasicMockType.client
            case JBasicMockType.server => BasicMockType.server
            case JBasicMockType.agent  => BasicMockType.agent
            case JBasicMockType.p2p    => BasicMockType.p2p
            case JBasicMockType.none   => BasicMockType.none
        }
    }

    implicit def javaToScala(jConType: JConnectionType): ConnectionType = {
        jConType match {
            case JConnectionType.tcp    => ConnectionType.tcp
            case JConnectionType.udp    => ConnectionType.udp
            case JConnectionType.http   => ConnectionType.http
            case JConnectionType.none   => ConnectionType.none
        }
    }

    implicit def javaToScala(result: JMockResult): (Array[Byte], FaultLevel) = {
        (result.getResponse, javaToScala(result.getFault, result.getData))
    }

    implicit def javaToScala(config: JConfiguration): Configuration = {
        new Configuration(
            config.getThreadNumber,
            config.getRepetitions,
            config.getServerPort,
            config.getTargetPort,
            config.getTargetIp,
            DEFAULT_IP,
            config.getMockNumber,
            config.getMockType,
            config.getMockConnection
        )
    }

    implicit def javaToScala(jContainer: JMockUnitContainer): MockUnitContainer = {
        new MockUnitContainer(
            jContainer.getMockName,
            jContainer.getMockClass,
            jContainer.getConfig
        )
    }

    implicit def javaToScala(jMockUnits: java.util.List[JMockUnitContainer]): List[MockUnitContainer] = {
        val convert = new ListBuffer[MockUnitContainer]

        val iterator = jMockUnits.iterator

        while (iterator.hasNext) {
            convert += iterator.next
        }
        convert.toList
    }

    implicit def javaToScala(handler: java.util.function.Function[HttpRequest, HttpResponse])
                    : HttpRequest => HttpResponse = {
        request => handler.apply(request)
    }

    @throws(classOf[IllegalArgumentException])
    def javaToScala(jFault: JFaultLevel, jData: JFaultLevelData): FaultLevel = {
        jFault match {
            case JFaultLevel.noFault            => new NoFault
            case JFaultLevel.fixedDelay         => new FixedDelay(jData.getTime)
            case JFaultLevel.looseResponse      => new LooseResponse
            case JFaultLevel.looseConnection    => new LooseConnection
            case JFaultLevel.multipleResponses  => new MultipleResponses(jData.getFactor)
            case _ => throw new IllegalArgumentException("no level available")
        }
    }

}
