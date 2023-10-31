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
        Short tipoOperacion = dis.readShort();

        Short tam = dis.readShort();

        byte[] datos = new byte[tam];
        dis.readFully(datos);

        Mensaje m = new Mensaje();
        m.setTipoOperacion(tipoOperacion);
        m.setDatos(datos);

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