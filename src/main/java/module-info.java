module com.example.together {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires com.google.gson;


    opens com.example.together to javafx.fxml, com.google.gson;
    exports com.example.together;
    exports com.example.together.model;
    opens com.example.together.model to javafx.fxml, com.google.gson;
    exports com.example.together.dboperations;
    opens com.example.together.dboperations to javafx.fxml, com.google.gson;
    exports com.example.together.controller;
    opens com.example.together.controller to javafx.fxml, com.google.gson;
    exports com.example.together.view;
    opens com.example.together.view to com.google.gson, javafx.fxml;
}