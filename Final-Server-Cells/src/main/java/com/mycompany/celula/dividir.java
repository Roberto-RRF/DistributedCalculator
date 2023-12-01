/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.mycompany.celula;

/**
 *
 * @author LUIS1
 */

public class dividir implements OperacionAritmetica {
    @Override
    public Float resuelve(Float n1, Float n2) {
        float resultado= Float.POSITIVE_INFINITY;
        if(n2!=0){
            resultado= n1 / n2; // Realizar la operación deseada, en este caso, division
        }   
        return resultado;
    }

}
                    
 