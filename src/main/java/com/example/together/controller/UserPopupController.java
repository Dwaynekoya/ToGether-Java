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

    /**
     * Assigns values to fields in popup
     */
    @FXML
    public void initialize() {
        labelUsername.setText(Utils.selectedUser.getUsername());
        labelBio.setText(Utils.selectedUser.getBio());
        imageview.setImage(new Image(Utils.selectedUser.getIcon()));
    }

    /**
     * Closes popup
     * @param actionEvent close button
     */
    @FXML
    private void closePopup(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
