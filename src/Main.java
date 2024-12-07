import java.io.IOException;

public class Main {
    public static void main(String ... args) {
        try {
            // Inicializa na porta 2121
            FTPServer server = new FTPServer(2121);
            server.start();
        } catch (IOException e ) {
            System.out.println("Error: " + e.getMessage() + ". Contact technical support.");
        }
    }
}
