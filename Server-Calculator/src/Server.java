import java.io.*;
import java.net.*;
import java.util.Objects;

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
                    String incomingMessage = serverIn.readUTF();
                    String[] messageParts = incomingMessage.split(",");
                    int result = 0;
                    if(Objects.equals(messageParts[0], "100"))
                    {
                        System.out.println("Operation code accepted. CODE: "+messageParts[0]);
                        switch (messageParts[3])
                        {
                            case "+":
                                System.out.println("Addition");
                                result = Integer.parseInt(messageParts[1]) + Integer.parseInt(messageParts[2]);

                                break;
                            case "-":
                                System.out.println("Subtraction");
                                result = Integer.parseInt(messageParts[1]) - Integer.parseInt(messageParts[2]);
                                break;
                            case "*":
                                System.out.println("Multiplication");
                                result = Integer.parseInt(messageParts[1]) * Integer.parseInt(messageParts[2]);
                                break;
                            case "/":
                                System.out.println("Division");
                                result = Integer.parseInt(messageParts[1]) / Integer.parseInt(messageParts[2]);
                                break;
                            default:
                                System.out.println("Operator not recognized: "+messageParts[3]);
                                break;
                        }

                        // make result string
                        String resultString = "200,"+messageParts[1]+","+messageParts[2]+","+messageParts[3]+","+result;

                        //Send result to MOM
                        out.writeUTF(resultString);


                    }
                    else
                    {
                        System.out.println("Operation code NOT accepted. CODE: "+messageParts[0]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}