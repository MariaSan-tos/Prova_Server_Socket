import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class FTPServer {

    private final int port;

    public FTPServer() {
        this.port = 2121;
    }

    public FTPServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        // Cria o ServerSocket na porta 2121 usando o host 127.0.0.1
        try (ServerSocket server = new ServerSocket(this.port, 2048, InetAddress.getByName("127.0.0.1"))) {
            System.out.println("Server started on port " + this.port + ". Use 'telnet 127.0.0.1 2121' to connect.");

            while (true) {
                // Inicia novas threads
                new Thread(new Client(server.accept())).start();
            }
        }
    }
}
