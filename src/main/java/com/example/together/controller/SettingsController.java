package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SettingsController {
    public Button settingsButton, groupButton, listButton, homeButton;
    public VBox confirmPopup;
    private int action; //1 means DELETE TASKS, 2 means LEAVE GROUPS

    /**
     * Sets up sidebar buttons
     */
    public void initialize(){
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
    }

    public void logOut(ActionEvent actionEvent) {
        Utils.loggedInUser=null;
        ViewSwitcher.switchView(View.LOGIN);
    }

    public void deleteAllTasks(ActionEvent actionEvent) {
        action=1;
        confirmPopup.setVisible(true);
    }

    public void leaveAllGroups(ActionEvent actionEvent) {
        action=2;
        confirmPopup.setVisible(true);
    }

    public void dismissPopup(ActionEvent actionEvent) {
        confirmPopup.setVisible(false);
    }

    public void confirmAction(ActionEvent actionEvent) {
        switch (action){
            case 1 -> DBTask.deleteAllTasks();
            case 2 -> DBGroup.leaveAllGroups();
            default -> System.out.println("Not a valid value for identifying the action");
        }
        action=0;
    }
    //TODO: Delete all (own) tasks, leave all groups, sign out
}
