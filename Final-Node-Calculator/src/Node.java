import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Node{

    public ServerSocket nodeSocket;
    public int maxPort = 5010;

    public Node(ServerSocket socket)
    {
        this.nodeSocket = socket;
    }





    public void startNode()
    {
        new Thread(() -> {
            try {
                while (!nodeSocket.isClosed()) {
                    Socket socket = nodeSocket.accept();
                    Manager nodeManager = new Manager(socket, false);
                    Thread thread = new Thread(nodeManager);
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void searchNodes()
    {
        new Thread(() -> {
            int port = 5000;
            while(port <= maxPort)
            {
                try
                {
                    Socket socket = new Socket("localhost",port);
                    Manager nodeManager = new Manager(socket, true);
                    Thread thread = new Thread(nodeManager);
                    thread.start();
                    port++;
                } catch (IOException e)
                {
                    if(port <= maxPort) {
                        port++;
                    } else {
                        e.printStackTrace();
                        System.out.println("No ports available");
                        break;
                    }
                }
            }
        }).start();
    }







}