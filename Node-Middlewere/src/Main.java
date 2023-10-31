import java.io.IOException;
import java.net.ServerSocket;

/*******************************************************************************
 *                             class Main
 *******************************************************************************
 * File       : Main.java
 * Author     : Roberto Requejo Fernandez
 * Date       : October 31, 2023
 * Description: Main class of the project. It starts the node.

 *******************************************************************************/

public class Main
{
    public static void main(String[] args)
    {
        Node node;
        int maxPort = 5010;
        int port = 5000;

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