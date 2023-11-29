package com.example.clientcalculator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

public class CalculatorController {
    @FXML
    private TextField calculatorDisplay;
    @FXML
    private VBox vbox;


    ArrayList<String> resultsHistory = new ArrayList<>();

    private String firstNumber="";
    private String secondNumber="";
    private String operator="";
    String serverResponse = "";
    String  packageToSend = "";


    private Socket socket;
    private DataInputStream  in;
    private DataOutputStream out;
    private SocketManager socketManager;

    int maxPort = 5010;
    int minPort = 5000;
    int maxAttempts = maxPort - minPort;



    @FXML
    private void numberButtonClickHandler(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        if(operator.isEmpty())
        {
            firstNumber += buttonText;
        }
        else
        {
            secondNumber += buttonText;
        }

        calculatorDisplay.setText(firstNumber+operator+secondNumber);
    }

    @FXML
    private void operatorButtonClickHandler(ActionEvent event){
        if(!firstNumber.isEmpty()){
            Button clickedButton = (Button) event.getSource();
            String buttonText = clickedButton.getText();
            operator = buttonText;
            calculatorDisplay.setText(firstNumber+operator+secondNumber);
        }
    }

    @FXML
    private void equalButtonClickHandler(ActionEvent event) throws IOException {
        Mensaje mensaje = new Mensaje();
        if(Objects.equals(operator, "+"))
        {
            mensaje.setTipoOperacion((short) 1);
        }
        else if(Objects.equals(operator, "-"))
        {
            mensaje.setTipoOperacion((short) 2);
        }
        else if(Objects.equals(operator, "*"))
        {
            mensaje.setTipoOperacion((short) 3);
        }
        else if(Objects.equals(operator, "/"))
        {
            mensaje.setTipoOperacion((short) 4);
        }

        packageToSend = firstNumber+","+secondNumber;

        mensaje.setDatos(packageToSend.getBytes());

        socketManager.sendPackage(mensaje);


        firstNumber = "";
        secondNumber = "";
        operator = "";
        calculatorDisplay.setText("");
    }

    @FXML
    private void clearButtonClickHandler(ActionEvent event){
        firstNumber = "";
        secondNumber = "";
        operator = "";
        calculatorDisplay.setText("");
    }

    private static int selectRandomPort(int lowerBound, int upperBound)
    {
        // Creating a Random object
        Random random = new Random();
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }


    public void initialize()
    {
        socketManager = new SocketManager(this);
        socketManager.startSocketThread();
    }

    public void updateUIWithResult(String result) {
        resultsHistory.add(result);
        Platform.runLater(() -> {
            Label resultLabel = new Label(result);
            vbox.getChildren().add(resultLabel);
        });
    }

}