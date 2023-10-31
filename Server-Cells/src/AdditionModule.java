import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class AdditionModule {
    private Socket socket;

    private DataInputStream  in;

    private DataOutputStream out;

    public int maxPort = 5010;
    public int minPort = 5000;
    public int maxAttempts = maxPort - minPort;

    public AdditionModule(String serverName)
    {

        while(maxAttempts > 0)
        {
            int nodePort = selectRandomPort(minPort, maxPort);

            try
            {
                socket = new Socket(serverName, nodePort);
                System.out.println("Connected to Node on port: " + nodePort);

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("cell");

                // Create a thread to listen for server messages
                Thread messageListener = new Thread(new MessageListener());
                messageListener.start();
                maxAttempts = 0;

            } catch (IOException e)
            {
                if(maxAttempts > 0)
                {
                    maxAttempts--;
                } else
                {
                    e.printStackTrace();
                    System.out.println("No ports available");
                    break;
                }
            }
        }

    }

    private static int selectRandomPort(int lowerBound, int upperBound)
    {
        // Creating a Random object
        Random random = new Random();
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    private class MessageListener implements Runnable {
        @Override
        public void run() {

            while (socket.isConnected()) {
                try {
                    Mensaje incoming = DecoderEncoder.leer(in);
                    //incomingMessage = in.readUTF();
                    String incomingMessage = new String(incoming.getDatos());
                    String[] messageParts = incomingMessage.split(",");
                    if (incoming.getTipoOperacion() == (short) 1)
                    {
                        System.out.println("Addition module received package");
                        int result = Integer.parseInt(messageParts[0]) + Integer.parseInt(messageParts[1]);

                        String resultMessage = messageParts[0] + " + " + messageParts[1] + " = " + result;
                        Mensaje outgoing = new Mensaje();
                        outgoing.setTipoOperacion((short) 5);
                        outgoing.setDatos(resultMessage.getBytes());

                        DecoderEncoder.escribir(out, outgoing);
                    }
                } catch (IOException e) {
                    System.out.println("Error receiving package from component");
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    // main
    public static void main(String[] args) {
        new AdditionModule("localhost");
    }

}