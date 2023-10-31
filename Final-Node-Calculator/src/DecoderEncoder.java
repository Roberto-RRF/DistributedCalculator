import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
/**
 *
 * @author Sebastian Godinez Borja
 */
public class DecoderEncoder {



    public static Mensaje leer(DataInputStream dis) throws IOException {
        // ciclo de lectura

        try{
            Short tipoOperacion = dis.readShort(); // se lee el tipo de operacion
            Short tam = dis.readShort(); // se lee el tam del arreglo
            byte[] datos = new byte[tam]; // Definir el arreglo de bytes
            dis.readFully(datos); // leer el arreglo de datos
            Mensaje m = new Mensaje();
            m.setTipoOperacion(tipoOperacion);
            m.setDatos(datos);
            return m;
        } catch (IOException e){
            System.out.println("Error al leer");
            return null;
        }

    }

    public static void escribir(DataOutputStream dos, Mensaje mensaje) throws IOException {
        try {
            Short tam = (short) mensaje.getDatos().length;

            dos.writeShort(mensaje.getTipoOperacion());

            dos.writeShort(tam);

            dos.write(mensaje.getDatos());
        } catch (IOException e){
            System.out.println("Error al escribir");
        }
    }

}
