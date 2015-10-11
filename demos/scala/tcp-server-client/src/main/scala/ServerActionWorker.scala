import java.io.{ObjectOutputStream, ObjectInputStream}
import java.net.ServerSocket

object  ServerActionWorker {

    val serverPort = 12345

    def init(repetitions: Int): Thread = {
        val worker = new Thread(new ServerActionWorker(repetitions))

        worker.start
        worker
    }

}

class ServerActionWorker(
                            val repetitions: Int
                        ) extends Runnable {

    import ServerActionWorker._

    val server      = new ServerSocket(serverPort)

    override def run: Unit = {
        var counter = 0

        while (counter < repetitions) {
            val client = server.accept
            val inStream = new ObjectInputStream(client.getInputStream)

            val number = inStream.readInt

            val outStream = new ObjectOutputStream(client.getOutputStream)

            outStream.writeInt(number * 2)
            outStream.flush

            client.close
            counter += 1
        }
    }

}
