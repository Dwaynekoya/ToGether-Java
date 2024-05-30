package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.model.Group;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;

public class NewGroupController {
    public TextField textfieldName;
    public TextArea textAreaInfo;
    public Label labelRequiredFields;
    public Button settingsButton, groupButton,listButton,homeButton;
    private String groupName, description;

    public void createGroup(ActionEvent actionEvent) {
        if (takeInputData()){
            Group newGroup = new Group(groupName,description);
            DBGroup.createGroup(newGroup);
        } else  {
            labelRequiredFields.setVisible(true);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        ViewSwitcher.switchView(View.TASKLIST);
    }
    @FXML
    public void initialize() {
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
    }
    /**
     * Takes the data from the input fields present on the fxml view
     * @return true if all needed fields have been filled, false if not
     */
    private boolean takeInputData() {
        groupName = textfieldName.getText();
        description = textAreaInfo.getText();
        return Utils.checkDataValidity(new ArrayList<>(Arrays.asList(groupName,description)));

    }
}
