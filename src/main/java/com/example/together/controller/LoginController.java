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
import java.util.ArrayList;
import java.util.Arrays;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label infoLabel;
    private String username;
    private String password;

    /**
     *  Takes data from the input fields and uses it to log into the app
     * @param actionEvent Login button
     * @throws Exception if it fails to connect to the database or retrieve its data
     */
    public void login(ActionEvent actionEvent) throws Exception {
        if (!takeInputData()){
            return;
        }
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

    /**
     * Switches view to the main app
     * @param loginID to identify the current user
     */
    private void launchApp(int loginID) {
        Utils.loggedInUser = DBUsers.getUser(loginID);
        ViewSwitcher.switchView(View.TASKLIST);
    }

    /**
     * Creates a new user in the database taking the input from the textfields
     * @param actionEvent sign up button
     */
    public void signup(ActionEvent actionEvent) {
        takeInputData();
        int newID = DBUsers.registerUser(username,password);
        switch (newID){
            case -1 -> infoLabel.setText("Error registering the user. Try again later :(");
            case -2 -> infoLabel.setText("Username has already been registered.");
            default -> infoLabel.setText("User has been registered!");
        }
    }

    /**
     * Gets text from the different textfields in the view
     *
     * @return
     */
    private boolean takeInputData() {
        username = usernameField.getText();
        password = passwordField.getText();
        //checks if all necessary data has been filled in:
        if (!Utils.checkDataValidity(new ArrayList<>(Arrays.asList(username, password)))){
            infoLabel.setText("Please fill in your username and password.");
            return false;
        } else {
            return true;
        }
    }
}