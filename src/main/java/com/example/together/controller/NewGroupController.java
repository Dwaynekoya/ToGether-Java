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

    /**
     * Creates a new group using the user inputted data, then switches back view
     * @param actionEvent create button
     */
    public void createGroup(ActionEvent actionEvent) {
        if (takeInputData()){
            Group newGroup = new Group(groupName,description);
            DBGroup.createGroup(newGroup);
            ViewSwitcher.switchView(View.PROFILE);
        } else  {
            labelRequiredFields.setVisible(true);
        }
    }

    /**
     * Goes back to previous view
     * @param actionEvent cancel button
     */
    public void cancel(ActionEvent actionEvent) {
        ViewSwitcher.switchView(View.PROFILE);
    }

    /**
     * Sets up the sidebar
     */
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
