package com.example.clientcalculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class SocketManager {

    private final CalculatorController controller;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public String clientId = "";

    private int maxPort = 5010;
    private int minPort = 5000;
    private int maxAttempts = (maxPort - minPort)*10;

    public SocketManager(CalculatorController controller) {
        this.controller = controller;
    }



    public void startSocketThread() {
        Thread socketThread = new Thread(() -> {
            try {
                while (maxAttempts > 0) {
                    try {
                        socket = new Socket("localhost", selectRandomPort(minPort, maxPort));
                        System.out.println("Connected to server on port: " + socket.getPort());
                        in = new DataInputStream(socket.getInputStream());
                        out = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));

                        out.writeUTF("cell");
                        out.flush();
                        break;
                    } catch (IOException e) {
                        if (maxAttempts > 0) {
                            maxAttempts--;
                        } else {
                            e.printStackTrace();
                            System.out.println("No ports available");
                            break;
                        }
                    }
                }

                // Creamos un hash con el puerto, la ip y la fecha para identificar al cliente
                clientId = socket.getLocalAddress().toString() + socket.getLocalPort() + System.currentTimeMillis();
                System.out.println("Se genero el siguiente id para el cliente: " + clientId);

                while (true) {
                    System.out.println("Waiting for message");
                    Mensaje incoming = DecoderEncoder.leer(in);

                    if (incoming.getTipoOperacion() == (short) 99) {
                        QueueManager.getInstance().registrarAcuse(new String(incoming.getIdOperacion()), incoming);
                    }
                    if (incoming.getTipoOperacion() == (short) 5 && QueueManager.getInstance().esAcusado(incoming)) { //
                        controller.updateUIWithResult(new String(incoming.getDatos()));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        socketThread.setDaemon(true);
        socketThread.start();

        QueueManager.getInstance().getNextMessage(out);
    }

    public void sendPackage(Mensaje mensaje) {
        try {
            System.out.println("Sending message");
            DecoderEncoder.escribir(out, mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int selectRandomPort(int lowerBound, int upperBound) {
        Random random = new Random();
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    public String getLocalPort() {
        return String.valueOf(socket.getLocalPort());
    }
}