package com.example.together.controller;

import javafx.scene.control.Button;

public class SettingsController {
    public Button settingsButton, groupButton, listButton, homeButton;

    /**
     * Sets up sidebar buttons
     */
    public void initialize(){
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
    }
    //TODO: Delete all (own) tasks, leave all groups, sign out
}
