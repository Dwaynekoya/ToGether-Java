package com.example.together.controller;

import com.example.together.model.User;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.scene.control.Button;

import java.util.List;

public class Utils {
    public static User loggedInUser;
    /**
     * Used to determine if the user has inputted all the data necessary to create an object
     * @param strings: fields that the user must fill
     * @return false if one or more of the fields have not been filled, true if all have assigned values
     */
    public static boolean checkDataValidity(List<String> strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void sidebarSetup(Button settingsButton, Button groupButton, Button listButton, Button homeButton) {
        //TODO: Settings
        //settingsButton.setOnAction(actionEvent -> ViewSwitcher.switchView());
        //TODO: Social view
        //groupButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.GROUPS));
        listButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.TASKLIST));
        //TODO:Feed
        homeButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.TASKLIST));
    }
}
