import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*******************************************************************************
 *                             class DecoderEncoder
 *******************************************************************************
 * File       : DecoderEncoder.java
 * Author     : Sebastian Godinez Borja
 * Date       : October 31, 2023
 * Description: Class that encodes and decodes messages in bytes form. It is used
 *              to send and receive messages between the nodes and cells.

 *******************************************************************************/

public class DecoderEncoder
{
    public static Mensaje leer(DataInputStream dis) throws IOException
    {
// ciclo de lectura

        Short tipoOperacion = dis.readShort(); // se lee el tipo de operacion
        System.out.println("tipo entrada: "+tipoOperacion);

        Short tam = dis.readShort(); // se lee el tam del arreglo
        System.out.println("tam entrada: "+tam);

        byte[] datos = new byte[tam]; // Definir el arreglo de bytes
        dis.readFully(datos); // leer el arreglo de datos
        System.out.println("datos entrada: "+datos);

        String mensaje = new String(datos); // se imprime en pantalla
        System.out.println("datos entrada: "+ mensaje);

        Mensaje m = new Mensaje();
        m.setTipoOperacion(tipoOperacion);
        System.out.println("tipo: "+m.getTipoOperacion());

        m.setDatos(datos);
        String mensaje2 = new String(m.getDatos());
        System.out.println("LEER: datos: "+mensaje2);



        return m;
    }

    public static void escribir(DataOutputStream dos, Mensaje mensaje) throws IOException
    {
        Short tam = (short) mensaje.getDatos().length;

        dos.writeShort(mensaje.getTipoOperacion());
        dos.writeShort(tam);
        dos.write(mensaje.getDatos());
    }
}