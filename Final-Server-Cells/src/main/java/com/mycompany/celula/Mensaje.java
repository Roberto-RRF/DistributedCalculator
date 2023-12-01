package main.java.com.mycompany.celula;

public class Mensaje
{

    Short tipoOperacion;
    byte[] datos;
    byte[] idOperacion;
    byte[] idClient;
    byte[] idCell;

    // Constructor that sets everything to null
    public Mensaje() {
        this.tipoOperacion = null;
        this.datos = null;
        this.idOperacion = null;
        this.idClient = null;
        this.idCell = null;
    }

    public Short getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(Short tipoOperacion) { this.tipoOperacion = tipoOperacion; }
    public byte[] getIdOperacion() { return idOperacion; }
    public void setIdOperacion(byte[] idOperacion) { this.idOperacion = idOperacion; }
    public byte[] getIdCell() { return idClient; }
    public void setIdCell(byte[] idCell) { this.idClient = idCell; }
    public byte[] getIdNode() { return idCell; }
    public void setIdNode(byte[] idNode) { this.idCell = idNode; }
    public byte[] getDatos() { return datos; }
    public void setDatos(byte[] datos) { this.datos = datos; }

    public void printVariables() {
        System.out.println("Tipo Operacion: " + (tipoOperacion != null ? tipoOperacion : "null"));
        System.out.println("Datos: " + (datos != null ? new String(datos) : "null"));
        System.out.println("ID Operacion: " + (idOperacion != null ? new String(idOperacion) : "null"));
        System.out.println("ID Cell: " + (idClient != null ? new String(idClient) : "null"));
        System.out.println("ID Node: " + (idCell != null ? new String(idCell) : "null"));
    }

}
