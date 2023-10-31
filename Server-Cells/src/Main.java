import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws Exception
    {
        File doc = new File("C:\\Users\\rrobe\\OneDrive\\Escritorio\\DistributedCalculator\\Server-Cells\\src\\conf.txt");
        Scanner obj = new Scanner(doc);
        List<Integer> numberModules = new ArrayList<>();


        while (obj.hasNextLine())
        {
            numberModules.add(Integer.parseInt(obj.nextLine().split("=")[1].trim()));
        }



    }

}
