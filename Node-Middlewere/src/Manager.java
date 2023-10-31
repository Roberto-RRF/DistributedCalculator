import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/*******************************************************************************
 *                             class Manager
 *******************************************************************************
 * File       : Manager.java
 * Author     : Roberto Requejo Fernandez
 * Date       : October 31, 2023
 * Description: The definition of the manager. It contains necessary methods to
 *              manage the connections between nodes and cells. Here is where we
 *              listen for incoming messages and broadcast them to the other
 *              components.

 *******************************************************************************/


public class Manager implements Runnable
{

    public static ArrayList<Manager> managerList = new ArrayList<>();

    private Socket socket;

    private DataInputStream  in;

    private DataOutputStream out;

    private String type;


    public Manager(Socket socket, boolean knownNode)
    {
        try
        {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            if (knownNode)
            {
                out.writeUTF("node");
                this.type = "node";
            } else
            {
                this.type = in.readUTF();
            }
            managerList.add(this);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while (socket.isConnected())
        {
            try
            {
                Mensaje mensaje = DecoderEncoder.leer(in);

                if (Objects.equals(type, "node"))
                {
                    broadcastPackageToCells(mensaje);
                } else
                {
                    broadcastPackage(mensaje);
                }
            } catch (IOException e)
            {
                System.out.println("Error receiving package from component");
                e.printStackTrace();
                break;
            }
        }
    }

    public void broadcastPackage(Mensaje data)
    {
        for (Manager component : managerList)
        {
            try
            {
                if (!component.socket.equals(this.socket))
                {
                    DecoderEncoder.escribir(component.out, data);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void broadcastPackageToCells(Mensaje data)
    {
        for (Manager component : managerList)
        {
            try
            {
                if (!component.socket.equals(this.socket) && Objects.equals(component.type, "cell"))
                {
                    DecoderEncoder.escribir(component.out, data);
                }
            } catch (IOException e)
            {
                System.out.println("Error while broadcasting package to cells");
                e.printStackTrace();
            }
        }
    }
}