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

public class DecoderEncoder {



    public static Mensaje leer(DataInputStream dis) throws IOException
    {
        Mensaje m = new Mensaje();

        // Leer tipo de operacion
        Short tipoOperacion = dis.readShort();
        m.setTipoOperacion(tipoOperacion);

        // Leer Datos
        Short tam = dis.readShort();
        byte[] datos = new byte[tam];
        dis.readFully(datos);
        m.setDatos(datos);

        // Leer idOperacion
        tam = dis.readShort();
        byte[] idOperacion = new byte[tam];
        dis.readFully(idOperacion);
        m.setIdOperacion(idOperacion);

        // Leer idCell
        tam = dis.readShort();
        byte[] idCell = new byte[tam];
        dis.readFully(idCell);
        m.setIdCell(idCell);

        // Leer idNode
        tam = dis.readShort();
        byte[] idNode = new byte[tam];
        dis.readFully(idNode);
        m.setIdNode(idNode);
        m.printVariables();
        return m;
    }

    public static void escribir(DataOutputStream dos, Mensaje mensaje) throws IOException
    {
        mensaje.printVariables();
        // Enviar tipo de operacion
        Short tam = mensaje.getTipoOperacion() == null ? 0 : mensaje.getTipoOperacion();
        dos.writeShort(tam);

        // Enviar Datos
        tam = mensaje.getDatos() == null ? 0 : (short) mensaje.getDatos().length;
        dos.writeShort(tam);
        dos.write(mensaje.getDatos());

        // Enviar idOperacion
        tam = mensaje.getIdOperacion() == null ? 0 : (short) mensaje.getIdOperacion().length;
        dos.writeShort(tam);
        dos.write(mensaje.getIdOperacion() != null ? mensaje.getIdOperacion() : new byte[0]);

        // Enviar idCell
        tam = mensaje.getIdCell() == null ? 0 : (short) mensaje.getIdCell().length;
        dos.writeShort(tam);
        dos.write(mensaje.getIdCell() != null ? mensaje.getIdCell() : new byte[0]);

        // Enviar idNode
        tam = mensaje.getIdNode() == null ? 0 : (short) mensaje.getIdNode().length;
        dos.writeShort(tam);
        dos.write(mensaje.getIdNode() != null ? mensaje.getIdNode() : new byte[0]);
    }
}
