import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MOM {
    private ServerSocket server;
    private List<ClientHandler> clients;

    public MOM(int port) {
        clients = new ArrayList<>();

        try {
            server = new ServerSocket(port);
            System.out.println("MOM SERVER RUNNING ON PORT " + port);
            while (true) {


                // Accept a client connection
                Socket socket = server.accept();
                System.out.println("Client accepted");
                InetAddress clientAddress = socket.getInetAddress();
                int clientPort = socket.getPort();
                System.out.println("New connection from " + clientAddress.getHostAddress() + ":" + clientPort);


                // Create a new client handler and add it to the list
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);

                // Start the client handler in a separate thread
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        MOM server = new MOM(6666);
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());




                String line = "";

                while (true) {
                    line = in.readUTF();

                    // Broadcast the message to all connected clients
                    broadcastMessage(line);
                }
            } catch (IOException i) {
                System.out.println(i);
            } finally {
                System.out.println("Closing connection for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                // Remove this client handler from the list
                clients.remove(this);

                // Close connection
                try {
                    socket.close();
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Notify all clients that this client has left the chat
                broadcastMessage(socket.getInetAddress().getHostAddress() + ":" + socket.getPort()+" has disconnected");
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                try {
                    client.out.writeUTF(message);
                    client.out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}