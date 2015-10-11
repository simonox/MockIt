package org.mockit.core

import org.mockit.core.BasicMockType.BasicMockType
import org.mockit.core.ConnectionType.ConnectionType

/**
 * Configuration container for a '''MockIt''' applications.
 *
 * @author  pheymann
 * @version 0.1.0
 */

/**
 * Basic configuration container with all possible parameters.
 * For a detailed description of the parameters have a look at
 * the subclasses.
 *
 * @see ServerConfiguration
 * @see ClientConfiguration
 * @see UDPConfiguration
 */
class Configuration (
                        val threadNumber:   Int,
                        val repetitions:    Int,
                        val serverPort:     Int,
                        val targetPort:     Int,
                        val targetIp:       String,

                        val originIp:       String,

                        val mockNumber:     Int,
                        val mockType:       BasicMockType,
                        val mockConnection: ConnectionType
                    ) extends Serializable

/**
 * Configuration container for server applications except UDP.
 *
 * @param serverPort
 *                  server port of the mock application
 * @param threadNumber
 *                  number of threads running to answer incoming
 *                  requests
 * @param mockConnection
 *                  connection type, e.g. tcp, http, ...
 */
case class ServerConfiguration(
                                override val serverPort:        Int,
                                override val threadNumber:      Int,
                                override val mockConnection:    ConnectionType
                              ) extends Configuration(
                                                        threadNumber,
                                                        DEFAULT_REPETITIONS,
                                                        serverPort,
                                                        STUFFING_INT,
                                                        STUFFING_STRING,
                                                        DEFAULT_IP,
                                                        DEFAULT_MOCK_NUM,
                                                        BasicMockType.server,
                                                        mockConnection
                                                      )

/**
 * Configuration container for client application except UDP.
 *
 * @param threadNumber
 *                  number of client threads on a [[org.mockit.core.MockAgent]]
 * @param repetitions
 *                  number of repetitions of the client behaviour on a [[org.mockit.core.MockAgent]]
 * @param targetPort
 *                  port of the server which the client has to contact
 * @param targetIp
 *                  ip of the server which the client has to contact
 * @param mockNumber
 *                  number of [[org.mockit.core.MockAgent]]s running with this client
 * @param mockConnection
 *                  connection type, e.g. tcp, http, ...
 */
case class ClientConfiguration(
                                override val threadNumber:      Int     = DEFAULT_THREAD_NUM,
                                override val repetitions:       Int     = DEFAULT_REPETITIONS,
                                override val targetPort:        Int     = DEFAULT_PORT,
                                override val targetIp:          String  = DEFAULT_IP,
                                override val mockNumber:        Int     = DEFAULT_MOCK_NUM,
                                override val mockConnection:    ConnectionType
                              ) extends Configuration(
                                                        threadNumber,
                                                        repetitions,
                                                        STUFFING_INT,
                                                        targetPort,
                                                        targetIp,
                                                        DEFAULT_IP,
                                                        mockNumber,
                                                        BasicMockType.client,
                                                        mockConnection
                                                     )

/**
 * Configuration container for udp applications that provides a bi-directional
 * communication channel.
 *
 * @param threadNumber
 *                  number of client threads
 * @param repetitions
 *                  number of repetitions of the client behaviour on a [[org.mockit.core.MockAgent]]
 * @param serverPort
 *                  reads data packages of the port if it is set
 * @param targetPort
 *                  sends data packages to the target port
 * @param targetIp
 *                  sends data packages to the target ip
 * @param mockNumber
 *                  number of [[org.mockit.core.MockAgent]]s running with this client
 */
case class UDPP2PConfiguration(
                                    override val threadNumber:      Int     = DEFAULT_THREAD_NUM,
                                    override val repetitions:       Int     = DEFAULT_REPETITIONS,
                                    override val serverPort:        Int     = DEFAULT_PORT,
                                    override val targetPort:        Int     = DEFAULT_PORT,
                                    override val targetIp:          String  = DEFAULT_IP,
                                    override val mockNumber:        Int     = DEFAULT_MOCK_NUM
                                ) extends Configuration(
                                                        threadNumber,
                                                        repetitions,
                                                        serverPort,
                                                        targetPort,
                                                        targetIp,
                                                        DEFAULT_IP,
                                                        mockNumber,
                                                        BasicMockType.p2p,
                                                        ConnectionType.udp
                                                       )