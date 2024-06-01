package com.example.together.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class UserPopupController {
    public ImageView imageview;
    public Label labelUsername, labelBio;

    @FXML
    public void initialize() {
        labelUsername.setText(Utils.selectedUser.getUsername());
        labelBio.setText(Utils.selectedUser.getBio());
        imageview.setImage(new Image(Utils.selectedUser.getIcon()));
    }
    @FXML
    private void closePopup(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
