package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.DBUsers;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SettingsController {
    @FXML
    public Button settingsButton, groupButton, listButton, homeButton;
    @FXML
    public VBox confirmPopup;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private int action; //1 means DELETE TASKS, 2 means LEAVE GROUPS

    /**
     * Sets up sidebar buttons
     */
    public void initialize(){
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
    }

    /**
     * Logs out current user and sends app back to the login view
     * @param actionEvent log out button
     */
    public void logOut(ActionEvent actionEvent) {
        Utils.loggedInUser=null;
        ViewSwitcher.switchView(View.LOGIN);
    }

    /**
     * opens confirmation box and assigns value (1) to action to identify current operation
     * @param actionEvent delete all tasks button
     */
    public void deleteAllTasks(ActionEvent actionEvent) {
        action=1;
        confirmPopup.setVisible(true);
    }

    /**
     * opens confirmation box and assigns value (2) to action to identify current operation
     * @param actionEvent leave all groups button
     */
    public void leaveAllGroups(ActionEvent actionEvent) {
        action=2;
        confirmPopup.setVisible(true);
    }

    /**
     * Closes confirmation popup
     * @param actionEvent cancel button
     */
    public void dismissPopup(ActionEvent actionEvent) {
        confirmPopup.setVisible(false);
    }

    /**
     * Confirms current operation/action
     * @param actionEvent confirm button
     */
    public void confirmAction(ActionEvent actionEvent) {
        switch (action){
            case 1 -> DBTask.deleteAllTasks();
            case 2 -> DBGroup.leaveAllGroups();
            default -> System.out.println("Not a valid value for identifying the action");
        }
        action=0;
    }

    /**
     * Changes the username of the logged-in user
     * @param actionEvent change username button
     */
    public void changeUsername(ActionEvent actionEvent) {
        String newUsername = usernameField.getText().trim();
        if (!newUsername.isEmpty()) {
            DBUsers.editUser("username", newUsername);
            Utils.loggedInUser.setUsername(newUsername);
            System.out.println("Username updated");
        } else {
            System.out.println("Username cannot be empty");
        }
    }

    /**
     * Changes the password of the logged-in user
     * @param actionEvent change password button
     */
    public void changePassword(ActionEvent actionEvent) {
        String newPassword = passwordField.getText().trim();
        if (!newPassword.isEmpty()) {
            DBUsers.editUser("password", newPassword);
            System.out.println("Password updated");
        } else {
            System.out.println("Password cannot be empty");
        }
    }
}
