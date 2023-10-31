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
                Mensaje mensaje = DecoderEncoder.leer(in);

                if (Objects.equals(type, "node"))
                {
                    System.out.println("Broadcast to cells");
                    broadcastPackageToCells(mensaje);
                } else
                {
                    System.out.println("General Broadcast");
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
        for (Manager componentAdmin : managerList) {
            try {
                // Que no se lo mande a el mismo y que no sea null
                if (!componentAdmin.socket.equals(this.socket)) {
                    DecoderEncoder.escribir(componentAdmin.out, data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void broadcastPackageToCells(Mensaje data)
    {
        System.out.println("Broadcasting package: " + data);
        for (Manager componentAdmin : managerList)
        {
            try
            {
                if (!componentAdmin.socket.equals(this.socket) && Objects.equals(componentAdmin.type, "cell"))
                {
                    DecoderEncoder.escribir(componentAdmin.out, data);
//                    out.writeUTF(data);
                }
            } catch (IOException e)
            {
                System.out.println("Error sending package to cell");
                e.printStackTrace();
            }
        }
    }
}