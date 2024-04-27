package com.example.together.controller;

import com.example.together.dboperations.DBUsers;
import com.example.together.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label infoLabel;
    private String username;
    private String password;
    private int loginUser; //TODO: pass to new window to identify the user on the app?

    public void login(ActionEvent actionEvent) {
        takeInputData();
        int loginID = DBUsers.login(username, password);
        switch (loginID){
            case -1 -> infoLabel.setText("Username not found.");
            case -2 -> infoLabel.setText("Incorrect password.");
            default -> launchApp(loginID);
        }
    }

    private void launchApp(int loginID) {
    }

    public void signup(ActionEvent actionEvent) {
        takeInputData();
        int newID = DBUsers.registerUser(username,password);
        switch (newID){
            case -1 -> infoLabel.setText("Error registering the user. Try again later :(");
            case -2 -> infoLabel.setText("Username has already been registered.");
            default -> infoLabel.setText("User has been registered!");
        }
    }

    private void takeInputData() {
        username = usernameField.getText();
        password = passwordField.getText();

    }
}