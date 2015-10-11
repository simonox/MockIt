package org.mockit.tool

import java.io.ObjectOutputStream
import java.net.Socket

import org.mockit.core.MessageProvider._
import org.mockit.core._
import org.mockit.logging.Logger

/**
 * Main procedure to run the tool.
 *
 * This tool provides services to start and stop '''MockIt-Daemons''' and
 * to show the current daemon network or some helping information.
 *   It is designed as console application.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object MockItTool extends App {

    val quit        = """(-q|--quit),([0-9]+)"""r
    val run         = """(-r|--run).*"""r
    val runTarget   = """(.)*,(-t|--target),([a-zA-Z0-9\\\/\.\_-]*)"""r
    val help        = """(-h)|(--help)"""r
    val register    = """--reg"""r

    val log         = new Logger {override lazy val name = "MockItTool"}

    var list = Nil

    if (args.length == 0)
        println(msg("no-command"))
    else {
        val input = args.mkString(",")

        input match {
            case quit(_, id) =>
                try {
                    val idConvert = id.toInt
                    val client = new Socket(DEFAULT_IP, MOCKIT_PORT + idConvert)
                    val outStream = new ObjectOutputStream(client.getOutputStream)

                    outStream.writeObject(ShutdownDaemon())
                    println(msg("await-shutdown").format(idConvert))

                    client.getInputStream.read

                    println(msg("remove-from-reg").format(idConvert))
                    MockItService.removeFromRegister(idConvert)

                    client.close()
                    log > s"MockIt-Daemon instance $id closed"
                }
                catch {
                    case e: Throwable =>
                        println(msg("no-valid-daemon").format(args(1)))
                        log error (s"can not close MockIt-Daemon $id", e)
                }
            case run(_*) =>
                val id = MockItService.persistInRegister
                val command = s"java -jar -Dlog_file_name=mockit_daemon_$id " + {
                    input match {
                        case runTarget(_, _, target) => target + "/"
                        case _ => ""
                    }
                } +
                    s"mockit_daemon.jar $id"

                log > s"run new MockIt-Daemon instance with command $command"
                Runtime.getRuntime.exec(command)
            case help(_*) => println(msg("help"))
            case register(_*) => println(MockItService.readRegister)
            case input: String => println(msg("no-valid-command").format(input))
        }
    }

}