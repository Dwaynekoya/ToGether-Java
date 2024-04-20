module com.example.together {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;


    opens com.example.together to javafx.fxml;
    exports com.example.together;
    exports com.example.together.model;
    opens com.example.together.model to javafx.fxml;
    exports com.example.together.dboperations;
    opens com.example.together.dboperations to javafx.fxml;
}