package org.mockit.tool

import java.io.FileWriter
import java.nio.file.{Paths, Files}
import java.text.SimpleDateFormat

import org.mockit.core._

import scala.io.Source

/**
 * Collection of service functions to modify the ''persistance file''
 * which stores the information about the running '''MockIt-Daemons'''.
 *
 * @author  pheymann
 * @version 0.1.0
 */
object MockItService {

    /**
     * Reads the current state of the register file.
     *
     * @return register
     */
    def readRegister: String = {
        if (Files.exists(Paths.get(REGISTER_FILE)))
            Source.fromFile(REGISTER_FILE).mkString
        else {
            MessageProvider.msg("no-daemon")
        }
    }

    /**
     * Stores a new entry into the register file with the pattern:
     *      [id], [execution date + time]
     * If no file exists a new one will be created.
     *
     * @return returns the id of the newly stored daemon entry
     */
    def persistInRegister: Int = {
        val formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

        var nextId = 0

        val register = {
            if (Files.exists(Paths.get(REGISTER_FILE)))
                Source.fromFile(REGISTER_FILE).getLines().toList
            else
                Nil
        }

        val writer = new FileWriter(REGISTER_FILE, false)

        if (register.nonEmpty) {
            nextId = register.head.split(",")(0).toInt + 1

            writer.write(nextId + "," + formatter.format(System.currentTimeMillis) + "\n")
            for (daemon <- register) {
                writer.write(daemon + "\n")
            }
        }
        else {
            writer.write(nextId + "," + formatter.format(System.currentTimeMillis) + "\n")
        }
        writer.flush()
        writer.close()
        nextId
    }

    /**
     * Removes an entry from the registry file by the given
     * daemon id.
     *
     * @param id
     *          identification of a '''MockIt-Daemon'''
     */
    def removeFromRegister(id: Int): Unit = {
        val register = {
            if (Files.exists(Paths.get(REGISTER_FILE)))
                Source.fromFile(REGISTER_FILE).getLines().toList
            else
                Nil
        }

        if (register.nonEmpty) {
            val writer = new FileWriter(REGISTER_FILE, false)

            if (register.size == 1)
                writer.write("")
            else {
                val currentId = register.head.split(",")(0)

                val newDaemons = register.filterNot(daemon => daemon.split(",")(0).equals(currentId))

                for (daemon <- newDaemons) {
                    writer.write(daemon + "\n")
                }
            }
            writer.flush()
            writer.close()
        }
    }

}
