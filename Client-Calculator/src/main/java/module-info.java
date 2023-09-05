module com.example.newclientcalculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.newclientcalculator to javafx.fxml;
    exports com.example.newclientcalculator;
}