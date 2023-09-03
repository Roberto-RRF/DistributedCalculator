import java.io.*;
import java.net.*;

public class Server {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Server(String serverName, int serverPort) {
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected to server");

            in = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            // Read and send the username to the server
            System.out.print("Enter your username: ");
            String username = in.readLine();
            out.writeUTF(username);

            // Create a thread to listen for server messages
            Thread messageListener = new Thread(new MessageListener());
            messageListener.start();

            // Start sending messages to the server
            String message;
            while (true) {
                message = in.readLine();
                out.writeUTF(message);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverName = "localhost"; // Change to the server's address if necessary
        int serverPort = 6666; // Use the server's port
        new Server(serverName, serverPort);
    }

    private class MessageListener implements Runnable {
        @Override
        public void run() {
            try {
                DataInputStream serverIn = new DataInputStream(socket.getInputStream());

                while (true) {
                    String message = serverIn.readUTF();
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}