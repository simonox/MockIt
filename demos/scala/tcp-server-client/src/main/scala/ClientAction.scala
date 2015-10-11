import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

class ClientAction(
                    val ip:     String,
                    val port:   Int
                  ) {

    def action(x: Int, y: Int): Int = {
        var result = 0

        val client = new Socket(ip, port)
        var outStream = new ObjectOutputStream(client.getOutputStream)
        outStream.writeInt(x)
        outStream.flush

        var stream = new ObjectInputStream(client.getInputStream)

        result += stream.readInt

        outStream = new ObjectOutputStream(client.getOutputStream)
        outStream.writeInt(y)
        outStream.flush

        stream = new ObjectInputStream(client.getInputStream)

        result += stream.readInt
        result
    }

}
