package com.example.together.controller;

import com.example.together.dboperations.DBUsers;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.ConnectException;

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

    public void login(ActionEvent actionEvent) throws Exception {
        takeInputData();
        int loginID = -3;
        try {
            loginID = DBUsers.login(username, password);
        } catch (ConnectException e){
            System.out.println("ERROR CONNECTING TO DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (loginID){
            case -1 -> infoLabel.setText("Username not found.");
            case -2 -> infoLabel.setText("Incorrect password.");
            case -3 -> infoLabel.setText("Could not connect to the database. Try again later.");
            default -> launchApp(loginID);
        }
    }

    private void launchApp(int loginID) throws Exception {
        Utils.loggedInUser = DBUsers.getUser(loginID);
        /*TaskListApplication taskListApplication = new TaskListApplication(loggedUser);
        taskListApplication.start(new Stage());*/
        ViewSwitcher.switchView(View.TASKLIST);
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