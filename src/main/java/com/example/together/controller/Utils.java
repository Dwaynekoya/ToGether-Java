package com.example.together.controller;

import com.example.together.model.User;

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
}
