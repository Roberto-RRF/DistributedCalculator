module com.example.clientcalculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.clientcalculator to javafx.fxml;
    exports com.example.clientcalculator;
}