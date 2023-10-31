package com.example.clientcalculator;

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
        System.out.println("datos entrada: "+datos);

        String mensaje = new String(datos); // se imprime en pantalla
        System.out.println("datos entrada: "+ mensaje);

        Mensaje m = new Mensaje();
        m.setTipoOperacion(tipoOperacion);
        System.out.println("tipo: "+m.getTipoOperacion());

        m.setDatos(datos);
        String mensaje2 = new String(m.getDatos());



        return m;
    }

    public static void escribir(DataOutputStream dos, Mensaje mensaje) throws IOException {

        Short tam = (short) mensaje.getDatos().length; // tam del arreglo
        System.out.println("tam salida: "+tam);

        dos.writeShort(mensaje.getTipoOperacion()); // enviar el tipo de operacion
        System.out.println("tipo salida: "+mensaje.getTipoOperacion());

        dos.writeShort(tam); // enviar el tam del mensaje
        System.out.println("tam salida: "+tam);

        dos.write(mensaje.getDatos()); // enviar el mensaje en bytes
        //System.out.println("datos salida: "+mensaje.getDatos());
        String mensaje2 = new String(mensaje.getDatos());
        System.out.println("datos salida: "+ mensaje2);

    }

}
