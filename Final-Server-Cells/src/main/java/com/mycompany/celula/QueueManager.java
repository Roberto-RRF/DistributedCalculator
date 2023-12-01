package main.java.com.mycompany.celula;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class QueueManager {
    private final List<Mensaje> operaciones = new ArrayList<>();
    private short cellType;
    private String cellId = "";

    private final String[] operacionesAritmeticas = {"+", "-", "*", "/"};

    private OperacionAritmetica operacionAritmetica;

    private QueueManager() {

    }

    public static QueueManager getInstance() {
        return ConfiguracionHolder.INSTANCE;
    }

    private static class ConfiguracionHolder
    {
        private static final QueueManager INSTANCE = new QueueManager();
    }

    public Mensaje getNextMessage(DataOutputStream out) throws MalformedURLException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("cfg/config.properties"))
        {
            try
            {
                properties.load(input);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            cellType = Short.parseShort(properties.getProperty("tipo_operacion"));
            System.out.println("ID Celltype "+cellType);
        } catch (IOException e)
        {
            // Manejar cualquier excepción de entrada/salida (IOException)
            e.printStackTrace();
        }

        cellId = cellId.equals("") ? UUID.randomUUID().toString() : cellId;

        if(cellType == 1)
        {
            operacionAritmetica = loadOperacionAritmetica("main.java.com.mycompany.celula.sumar", "dc/sumar.jar");
        }
        if(cellType == 2)
        {
            operacionAritmetica = loadOperacionAritmetica("main.java.com.mycompany.celula.restar", "dc/restar.jar");
        }
        if(cellType == 3)
        {
            operacionAritmetica = loadOperacionAritmetica("main.java.com.mycompany.celula.multiplicar", "dc/multiplicar.jar");
        }
        if(cellType == 4)
        {
            operacionAritmetica = loadOperacionAritmetica("main.java.com.mycompany.celula.dividir", "dc/dividir.jar");
        }
        new Thread(() -> {
            while (true) {
                while(operaciones.size() > 0)
                {
                    Mensaje mensaje = operaciones.get(0);
                    operaciones.remove(0);
                    try {
                        if(mensaje.getTipoOperacion() == cellType)
                        {
                            // Resolver operacion
                            String[] datos = new String(mensaje.getDatos()).split(",");
                            Float resultado = operacionAritmetica.resuelve(Float.parseFloat(datos[0]), Float.parseFloat(datos[1]));
                            System.out.println("Resultado: " + resultado);
                            String res = datos[0] + operacionesAritmeticas[cellType-1] + datos[1] + "=" + resultado.toString();
                            mensaje.setDatos(res.getBytes());
                            mensaje.setTipoOperacion((short) 5);

                            mensaje.setIdNode(cellId.getBytes());
                            DecoderEncoder.escribir(out, mensaje);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }



                try {
                    Thread.sleep(Duration.ofSeconds(1).toMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return null;
    }

    private OperacionAritmetica loadOperacionAritmetica(String classImp, String jarFile) throws MalformedURLException {
        try {
            URLClassLoader urlcl = URLClassLoader.newInstance(new URL[]{Paths.get(jarFile).toUri().toURL()},
                    ClassLoader.getSystemClassLoader());
            Class<?> clazz = urlcl.loadClass(classImp);
            Class<? extends OperacionAritmetica> oaClass = clazz.asSubclass(OperacionAritmetica.class);
            return oaClass.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar la clase: " + e.getMessage());
        } catch (ReflectiveOperationException e) {
            System.out.println("Error reflectivo: " + e.getMessage());
        }
        return null;
    }

    public void add(Mensaje mensaje) {
        operaciones.add(mensaje);
    }




}
