import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


public class Manager implements Runnable
{

    public static ArrayList<Manager> managerList = new ArrayList<>();

    private Socket socket;

    private DataInputStream  in;

    private DataOutputStream out;

    private String element;

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
        String incomingMessage;
        while (socket.isConnected())
        {
            try
            {
                incomingMessage = in.readUTF();
                System.out.println("Incoming message: " + incomingMessage);

                if (Objects.equals(type, "node"))
                {
                    System.out.println("Broadcast to cells");
                    broadcastPackageToCells(incomingMessage);
                } else
                {
                    System.out.println("General Broadcast");
                    broadcastPackage(incomingMessage);
                }
            } catch (IOException e)
            {
                System.out.println("Error receiving package from component");
                e.printStackTrace();
                break;
            }
        }
    }

    public void broadcastPackage(String data)
    {
        System.out.println("Broadcasting package: " + data);
        for (Manager componentAdmin : managerList)
        {
            try
            {
                if (!Objects.equals(componentAdmin.element, element))
                {
                    out.writeUTF(data);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void broadcastPackageToCells(String data)
    {
        System.out.println("Broadcasting package: " + data);
        for (Manager componentAdmin : managerList)
        {
            try
            {
                if (!componentAdmin.element.equals(element) && Objects.equals(componentAdmin.type, "cell"))
                {
                    out.writeUTF(data);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}