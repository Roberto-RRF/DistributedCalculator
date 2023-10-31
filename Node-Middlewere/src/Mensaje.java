/*******************************************************************************
 *                             class Mensaje
 *******************************************************************************
 * File       : DecoderEncoder.java
 * Author     : Sebastian Godinez Borja
 * Date       : October 31, 2023
 * Description: Class that defines the structure of the messages that are sent
 *              and received between the nodes and cells.

 *******************************************************************************/
public class Mensaje
{
    Short tipoOperacion;
    byte[] datos;

    public Short getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(Short tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "tipoOperacion=" + tipoOperacion + '}';
    }
}