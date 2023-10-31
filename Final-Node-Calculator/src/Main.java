import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main
{
    public static void main(String[] args)
    {
        Node node;
        int maxPort = 5010;
        int port = 5000;

        // Buscamos un ouerto del rango que este disponible
        while(port <= maxPort)
        {
            try
            {
                node = new Node(new ServerSocket(port));
                System.out.println("Node started on port: " + port);
                node.startNode();
                node.searchNodes();

                break;
            } catch (IOException e)
            {
                if(port <= maxPort)
                {
                    port++;
                } else
                {
                    e.printStackTrace();
                    System.out.println("No ports available");
                    break;
                }
            }
        }
    }
}