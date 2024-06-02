package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.model.Group;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GroupPopupController {
    public VBox nonOwnerBox;
    public Label labelName, labelDescription;
    public VBox ownerBox;
    public TextField textfieldName;
    public TextArea textAreaDescription;

    @FXML
    public void initialize() {
        System.out.println( "manager: " + Utils.selectedGroup.getManager());
        if (Utils.selectedGroup.getManager().equals(Utils.loggedInUser)){
            nonOwnerBox.setVisible(false);
            ownerBox.setVisible(true);

            this.textfieldName.setText(Utils.selectedGroup.getName());
            this.textAreaDescription.setText(Utils.selectedGroup.getDescription());
        } else {
            nonOwnerBox.setVisible(true);
            ownerBox.setVisible(false);

            this.labelName.setText(Utils.selectedGroup.getName());
            this.labelDescription.setText(Utils.selectedGroup.getDescription());
        }
    }
    public void saveGroupChanges(ActionEvent actionEvent) {
        String name = textfieldName.getText();
        String description = textAreaDescription.getText();

        Group group = Utils.selectedGroup;
        group.setName(name);
        group.setDescription(description);

        DBGroup.editGroup(group);
    }

    public void deleteGroup(ActionEvent actionEvent) {
        DBGroup.deleteGroup(Utils.selectedGroup);
    }

    public void closePopup(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
