package com.example.clientcalculator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class QueueManager {
    // definir una estructura de datos que alamcene los mensajes, cuidar concurrencia!!!
    private final Map<Short, List<Mensaje>> mensajesMap = new HashMap<>(); // Mapa de mapas "+", "-", "*", "/"
    private final Map<String, List<Mensaje>> acusesMap = new HashMap<>(); // Key: idOperacion, List: Mensajes


    private final Integer minNodes = 2;

    private QueueManager() {
    }

    public static QueueManager getInstance() {
        return ConfiguracionHolder.INSTANCE;
    }

    private static class ConfiguracionHolder {

        private static final QueueManager INSTANCE = new QueueManager();
    }

    public Mensaje getNextMessage(DataOutputStream out) {


        new Thread(() -> {
            while (true) {
                System.out.println("Queue Manager Thread!!!!!!!");
                for (Map.Entry<Short, List<Mensaje>> entry : mensajesMap.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    // 1. Get the first message from the list
                    List<Mensaje> mensajeList = entry.getValue();
                    if (mensajeList != null && !mensajeList.isEmpty()) {
                        Mensaje mensaje = mensajeList.get(0);

                        // 2. Check if the message has been acknowledged
                        if (esAcusado(mensaje)) {
                            // Borrar los acuses de la lista de acuses
                            acusesMap.remove(new String(mensaje.getIdOperacion()));
                            // Borrar el mensaje de la lista de mensajes
                            mensajeList.remove(0);
                            mensajesMap.put(entry.getKey(), mensajeList);// NOTA: No se si lo sobre escribe bien


                        } else {
                            // If not acknowledged, it would be good to resend it
                            try {
                                DecoderEncoder.escribir(out, mensaje);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(Duration.ofSeconds(10).toMillis());
                } catch (InterruptedException ex) {
                    System.out.println("Error in the resend thread");
                }
            }
        }).start();
        return null;
    }

    public Boolean esAcusado(Mensaje mensaje)
    {
        // Buscamos que en la lista de acuses exista el id de la operacion con 2 idcell diferentes
        // Si existe, entonces se ha recibido el mensaje en ambos nodos

        System.out.println("Buscando acuses para el mensaje: " + new String(mensaje.getIdOperacion()));
        List<Mensaje> acuses;
        if(acusesMap.containsKey(new String(mensaje.getIdOperacion())))
        {
            acuses = acusesMap.get(new String(mensaje.getIdOperacion()));

            if(acuses.size() >= minNodes)
            {
                return true;
            }
        }
        return false;
    }
    public void putMessage(Mensaje mensaje) {
        System.out.println("Se recibio el siguiente mensaje: ");
        mensaje.printVariables();

        List<Mensaje> list = mensajesMap.getOrDefault(mensaje.getTipoOperacion(), new ArrayList<>());
        list.add(mensaje);
        mensajesMap.put(mensaje.getTipoOperacion(), list);

        // Imprimir todo el contenido del mapa
        for (Map.Entry<Short, List<Mensaje>> entry : mensajesMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    public void registrarAcuse(String ack, Mensaje mensaje) {
        List<Mensaje> listaMensajes = acusesMap.getOrDefault(ack, new ArrayList<>());
        for (Mensaje m : listaMensajes) {
            // Comparar el idcell del mensaje entrante con el idcell de cada mensaje en la lista
            if (mensaje.getIdCell().equals(m.getIdCell())) {
                System.out.println("Ya existe el acuse con el mismo idcell");
                return;
            }
        }

        listaMensajes.add(mensaje);
        acusesMap.put(ack, listaMensajes);

    }
}
