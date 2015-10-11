import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientAction {

    private final int     port;

    private final String  ip;

    public ClientAction(final int port, final String ip) {
        this.port = port;

        this.ip = ip;
    }

    public int action(final int x, final int y) throws Exception {
        int result = 0;

        final Socket client = new Socket(ip, port);

        ObjectOutputStream outStream = new ObjectOutputStream(client.getOutputStream());
        outStream.writeInt(x);
        outStream.flush();

        ObjectInputStream stream = new ObjectInputStream(client.getInputStream());

        result += stream.readInt();

        outStream = new ObjectOutputStream(client.getOutputStream());
        outStream.writeInt(y);
        outStream.flush();

        stream = new ObjectInputStream(client.getInputStream());

        result += stream.readInt();
        return result;
    }

    public final int getPort() {
        return port;
    }

    public final String getIp() {
        return ip;
    }

}
