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

        Short tipoOperacion = dis.readShort(); // se lee el tipo de operacion
        System.out.println("tipo entrada: "+tipoOperacion);

        Short tam = dis.readShort(); // se lee el tam del arreglo
        System.out.println("tam entrada: "+tam);

        byte[] datos = new byte[tam]; // Definir el arreglo de bytes
        dis.readFully(datos); // leer el arreglo de datos
        System.out.println("datos entrada bytes: "+datos);

        String mensaje = new String(datos); // se imprime en pantalla
        System.out.println("datos entrada, string: "+ mensaje);

        Mensaje m = new Mensaje();
        m.setTipoOperacion(tipoOperacion);
        System.out.println("tipo: "+m.getTipoOperacion());

        m.setDatos(datos);



        return m;
    }

    public static void escribir(DataOutputStream dos, Mensaje mensaje) throws IOException {
        // tam del arreglo
        Short tam = (short) mensaje.getDatos().length;
        // enviar el tipo de operacion
        dos.writeShort(mensaje.getTipoOperacion());
        // enviar el tam del mensaje
        dos.writeShort(tam);
        // enviar el mensaje en bytes
        dos.write(mensaje.getDatos());
//        LOGGER.info("Mensaje enviado: " + mensaje);
    }

}
