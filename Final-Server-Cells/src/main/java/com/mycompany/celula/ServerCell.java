package main.java.com.mycompany.celula;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;
import java.net.URL;

/*******************************************************************************
 *                             class AdditionModule
 *******************************************************************************
 * File       : AdditionModule.java
 * Author     : Roberto Requejo Fernandez
 * Date       : October 31, 2023
 * Description: This class is the addition module of the calculator. It connects
 *              to the server and waits for messages from the server. When a
 *              message is received, it performs the addition and sends the
 *              result back to the server.

 *******************************************************************************/

public class ServerCell
{
    private Socket socket;

    private DataInputStream  in;

    private DataOutputStream out;

    public int maxPort = 5010;
    public int minPort = 5000;
    public int maxAttempts = (maxPort - minPort)*10;

    private OperacionAritmetica operacionAritmetica;




    public String cellId = "";
    public Short cellType;


    public ServerCell(String serverName) throws MalformedURLException {

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
        cellId = socket.getLocalAddress().toString() + socket.getLocalPort() + System.currentTimeMillis();

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("cfg/config.properties"))
        {
            try
            {
                properties.load(input);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            cellType = Short.parseShort(properties.getProperty("tipo_operacion"));
            System.out.println("ID Celltype "+cellType);
        } catch (IOException e)
        {
            // Manejar cualquier excepción de entrada/salida (IOException)
            e.printStackTrace();
        }


        QueueManager.getInstance().getNextMessage(out);





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
                    System.out.println("Waiting for message");
                    Mensaje incoming;
                    incoming = DecoderEncoder.leer(in);
                    incoming.printVariables();
                    if(incoming.getTipoOperacion() == (short) cellType)
                    {
                        QueueManager.getInstance().add(incoming);
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

    public static void main(String[] args) throws MalformedURLException { new ServerCell("localhost"); }
}