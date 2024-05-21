package com.example.together.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button settingsButton, groupButton, listButton, homeButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label bioLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO: load user info
        usernameLabel.setText(Utils.loggedInUser.getUsername());
        bioLabel.setText(Utils.loggedInUser.getBio());
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
    }
}
