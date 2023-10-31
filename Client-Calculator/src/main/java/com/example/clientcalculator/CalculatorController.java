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
    private void equalButtonClickHandler(ActionEvent event){


        packageToSend = "100,"+firstNumber+","+secondNumber+","+operator;

        firstNumber = "";
        secondNumber = "";
        operator = "";
        calculatorDisplay.setText("");

        try {
            out.writeUTF(packageToSend);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearButtonClickHandler(ActionEvent event){
        firstNumber = "";
        secondNumber = "";
        operator = "";
        calculatorDisplay.setText("");
    }


    public void initialize()
    {

        // Start a new thread for socket communication.
        Thread socketThread = new Thread(() -> {
            try {

                socket = new Socket("localhost", 5000);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));

                out.writeUTF("cell");
                out.flush();



                while (true) {

                    serverResponse = in.readUTF();
                    System.out.println(serverResponse);
                    String messageParts[] = serverResponse.split(",");
                    if(Objects.equals(messageParts[0], "200"))
                    {
                        String result = messageParts[1]+" "+messageParts[3]+" "+messageParts[2]+" = "+messageParts[4];
                        resultsHistory.add(result);
                        Platform.runLater(() -> {
                            Label resultLabel = new Label(result);
                            vbox.getChildren().add(resultLabel);
                        });
                    }
                    System.out.println(serverResponse);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        socketThread.setDaemon(true);
        socketThread.start();
    }


}