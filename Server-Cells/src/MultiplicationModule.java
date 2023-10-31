import java.io.*;
import java.net.Socket;
import java.util.Random;

/*******************************************************************************
 *                             class MultiplicationModule
 *******************************************************************************
 * File       : MultiplicationModule.java
 * Author     : Roberto Requejo Fernandez
 * Date       : October 31, 2023
 * Description: This class is the multiplication module of the calculator. It connects
 *              to the server and waits for messages from the server. When a
 *              message is received, it performs the multiplication and sends the
 *              result back to the server.

 *******************************************************************************/

public class MultiplicationModule
{
    private Socket socket;

    private DataInputStream  in;

    private DataOutputStream out;

    public int maxPort = 5010;
    public int minPort = 5000;
    public int maxAttempts = maxPort - minPort;

    public MultiplicationModule(String serverName)
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

    private class MessageListener implements Runnable
    {
        @Override
        public void run()
        {

            while (socket.isConnected())
            {
                try
                {
                    Mensaje incoming = DecoderEncoder.leer(in);
                    //incomingMessage = in.readUTF();
                    String incomingMessage = new String(incoming.getDatos());
                    String[] messageParts = incomingMessage.split(",");
                    if (incoming.getTipoOperacion() == (short) 3)
                    {
                        System.out.println("Multiplication module received package");
                        int result = Integer.parseInt(messageParts[0]) * Integer.parseInt(messageParts[1]);

                        String resultMessage = messageParts[0] + " * " + messageParts[1] + " = " + result;
                        Mensaje outgoing = new Mensaje();
                        outgoing.setTipoOperacion((short) 5);
                        outgoing.setDatos(resultMessage.getBytes());

                        DecoderEncoder.escribir(out, outgoing);
                    }
                } catch (IOException e)
                {
                    System.out.println("Error receiving package from component");
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new MultiplicationModule("localhost");
    }

}