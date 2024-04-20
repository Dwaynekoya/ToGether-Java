package com.example.together;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void login(ActionEvent actionEvent) {
        //TODO: Usar operaciones de BBDD en dboperations
    }

    public void signup(ActionEvent actionEvent) {
        //TODO: Crear usuario
    }
}