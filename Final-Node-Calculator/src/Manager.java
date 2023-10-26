import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


public class Manager implements Runnable{

    public static ArrayList<Manager> managerList = new ArrayList<>();

    private static int amountManagers = 0;

    private Socket socket;

    private DataInputStream  in;

    private DataOutputStream out;

    private String element;

    private String type;


    public Manager(Socket socket, boolean knownNode)
    {
        try{
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            if (knownNode) {
                out.writeUTF("node");
                this.type = "node";
                System.out.println("Node connected");
            } else {
                String temp = in.readUTF();
                System.out.println("Cell connected");
                this.type = temp;
            }
            amountManagers++;
            this.element = String.valueOf(amountManagers);
            managerList.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String incomingMessage;
        while (socket.isConnected()) {
            try {
                incomingMessage = in.readUTF();

                if (Objects.equals(type, "node")) {
                    System.out.println("Broadcast to cells");
                    broadcastPackageToCells(incomingMessage);
                } else{
                    System.out.println("General Broadcast");
                    broadcastPackage(incomingMessage);
                }

            } catch (IOException e) {
                System.out.println("Error receiving package from component");
                e.printStackTrace();
                break;
            }
        }
    }

    public void broadcastPackage(String data) {
        System.out.println("Broadcasting package: " + data);
        for (Manager componentAdmin : managerList) {
            try {
                if (!componentAdmin.element.equals(element)) {
                    componentAdmin.out.writeUTF(data);
                    componentAdmin.out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastPackageToCells(String data) {
        System.out.println("Broadcasting package: " + data);
        for (Manager componentAdmin : managerList) {
            try {
                if (!componentAdmin.element.equals(element) && Objects.equals(componentAdmin.type, "cell")) {
                    componentAdmin.out.writeUTF(data);
                    componentAdmin.out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
