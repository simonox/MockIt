import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActionWorker implements Runnable {

    public static final int     SERVER_PORT = 12345;

    private final int           repetitions;

    private final ServerSocket  server;

    public ServerActionWorker(final int repetitions) throws Exception {
        this.repetitions = repetitions;

        this.server = new ServerSocket(SERVER_PORT);
    }

    public void run() {
        int counter = 0;

        while (counter++ < repetitions) {
            try {
                final Socket client = server.accept();

                final ObjectInputStream inStream = new ObjectInputStream(client.getInputStream());

                final int number = inStream.readInt();

                final ObjectOutputStream outStream = new ObjectOutputStream(client.getOutputStream());

                outStream.writeInt(number * 2);
                outStream.flush();
                client.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Thread init(final int repetitions) throws Exception {
        final Thread worker = new Thread(new ServerActionWorker(repetitions));

        worker.start();
        return worker;
    }

}
