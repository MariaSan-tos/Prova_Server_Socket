import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {

    private final Socket socket;
    private static final String server_name = "FTPServer";

    // Define usuário e senha para autenticação
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";
    private boolean authenticated = false;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))
        ) {
            // Envia a mensagem inicial de boas-vindas
            out.println("220 " + this.server_name + " FTP Service Ready");

            String line; // Recebe as mensagens do cliente

            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line); //Printa as mensagens do cliente no console

                if (!authenticated) {
                    // Comando USER
                    if (line.startsWith("USER")) {
                        String username = line.substring(5).trim();
                        if (username.equals(USERNAME)) {
                            out.println("331 Password required");
                        } else {
                            out.println("530 Invalid username");
                        }
                    }
                    // Comando PASS
                    else if (line.startsWith("PASS")) {
                        String password = line.substring(5).trim();
                        if (password.equals(PASSWORD)) {
                            authenticated = true;
                            out.println("230 User logged in");
                        } else {
                            out.println("530 Login incorrect");
                        }
                    } else {
                        out.println("530 Please login with USER and PASS.");
                    }
                    continue;
                }

                // Comando LIST (após login)
                if (line.equals("LIST")) {
                    handleListCommand(out);
                }
                // Comando QUIT
                else if (line.equals("QUIT")) {
                    out.println("221 Goodbye");
                    break;
                } else {
                    out.println("500 Command not understood");
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally {
            try {
                socket.close(); //fecha o servidor
            } catch (IOException e) {
                System.err.println("Error closing Socket: " + e.getMessage());
            }
        }
    }

    // Abre nova conexão e envia lista de arquivos
    private void handleListCommand(PrintWriter out) {
        try {
            // Cria uma nova conexão passiva (ServerSocket)
            ServerSocket dataSocket = new ServerSocket(0); // Porta aleatória
            int dataPort = dataSocket.getLocalPort();
            String passiveModeResponse = getPassiveModeResponse(dataPort);
            out.println(passiveModeResponse);

            // Aguarda a conexão do cliente na porta de dados
            Socket dataConnection = dataSocket.accept();

            // Envia a lista de arquivos aleatorios
            List<String> files = getFilesList();
            PrintWriter dataOut = new PrintWriter(dataConnection.getOutputStream(), true);
            for (String file : files) {
                dataOut.println(file);
            }

            // Fecha
            dataConnection.close();
            dataSocket.close();

            out.println("226 Transfer complete"); // Resposta após o envio da lista

        } catch (IOException e) {
            out.println("550 Requested action not taken. Failed to send list of files.");
            System.err.println("Error sending files: " + e.getMessage());
        }
    }

    // Arquivos simulados para enviar
    private List<String> getFilesList() {
        List<String> files = new ArrayList<>();
        files.add("textone.txt");
        files.add("texttwo.txt");
        files.add("textthree.txt");
        return files;
    }

    // Gera a resposta para o modo passivo no formato FTP
    private String getPassiveModeResponse(int dataPort) {
        // Converte a porta para dois números separados por vírgula
        int port1 = dataPort / 256;
        int port2 = dataPort % 256;
        System.out.println("Data connection will be on port: " + dataPort + ". Use 'telnet 127.0.0.1 " + dataPort +"' to connect. ");
        return "227 Entering Passive Mode (127,0,0,1," + port1 + "," + port2 + ")";

    }
}
