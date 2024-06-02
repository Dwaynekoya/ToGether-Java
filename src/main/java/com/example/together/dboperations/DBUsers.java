package com.example.together.dboperations;

import com.example.together.controller.Utils;
import com.example.together.model.User;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;

public class DBUsers {
    /**
     * Looks for a given user in the table using a php file
     * @param username username that identifies the user to look for
     * @return -1 if not found, -2 if incorrect password, -3 if failed connection. Id if found
     */
    public static int login(String username, String password) throws Exception {
        URL url = new URL(Constants.login);
        String postData = String.format("username=%s&password=%s",
                URLEncoder.encode(username, StandardCharsets.UTF_8),
                URLEncoder.encode(password, StandardCharsets.UTF_8));

        String jsonResponse = DBGeneral.sendHttpPostRequest(url, postData);

        int userId = Integer.parseInt(jsonResponse.trim());
        return userId;
    }


    /**
     * Puts a new user into the table using a php file.
     * @param username unique String to identify user
     * @param password
     * @return -1 if failed, -2 if username already exists. If succesful, new user id
     */
    public static int registerUser(String username, String password) {
        int generatedId = -1;
        try {
            URL url = new URL(Constants.registerUser);
            String postData = String.format("username=%s&password=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8),
                    URLEncoder.encode(password, StandardCharsets.UTF_8));

            String jsonResponse = DBGeneral.sendHttpPostRequest(url, postData);

            generatedId = Integer.parseInt(jsonResponse.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    /**
     * Looks for user in database given its id
     * @param id int that identifies user
     * @return User object if found, null if not
     */
    public static User getUser(int id) {
        try {
            URL url = new URL(Constants.getUserGivenId + "?id=" + id);
            String response = DBGeneral.sendHttpGetRequest(url);
            Gson gson = new Gson();
            User user = gson.fromJson(response.toString(), User.class);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Looks for users with a LIKE query
     * @param username String to look for usernames containing it
     * @return ObservableList with matches
     */
    public static ObservableList<User> searchUsers(String username) {
        ObservableList<User> userList = FXCollections.observableArrayList();

        try {
            URL url = new URL(Constants.searchUsers);
            String postData = String.format("user_id=%d&username=%s", Utils.loggedInUser.getId(), username);
            String response = DBGeneral.sendHttpPostRequest(url, postData);

            userList.addAll(parseUsers(response));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * Adds a new line to the user_follows table
     * @param userId int that identifies current user
     * @param followsId int that identifies user to follow
     */
    public static void followUser(int userId, int followsId) {
        try {
            URL url = new URL(Constants.followUser);
            String postData = String.format("user_id=%d&follows_id=%d", userId, followsId);
            String response = DBGeneral.sendHttpPostRequest(url, postData);

            System.out.println("Follow User Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Looks for users a user follows
     * @param currentUser to extract id for the search
     * @return set of users followed by current user
     */
    public static HashSet<User> searchFollowing(User currentUser) {
        try {
            URL url = new URL(Constants.getFollowing);
            String postdata = String.format("user_id=%d", currentUser.getId());
            String jsonResponse = DBGeneral.sendHttpPostRequest(url, postdata);
            if (jsonResponse.equals("")) return null;

            HashSet<User> userHashSet = new HashSet<>();
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
            for (JsonElement jsonElement: jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int followsId = jsonObject.get("follows_id").getAsInt();
                User user = DBUsers.getUser(followsId);
                if (user!=null) userHashSet.add(user);
            }
            return userHashSet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to convert json array into a list of User objects
     * @param jsonResponse String with the json taken from the database query php
     * @return list of users from the json
     */
    private static List<User> parseUsers(String jsonResponse) {
        Gson gson = new Gson();
        Type userListType = new TypeToken<List<User>>(){}.getType();
        try {
            List<User> users = gson.fromJson(jsonResponse, userListType);
            return users;
        }catch (JsonSyntaxException exception){
            System.out.printf("EXCEPTION PARSING USERS: %s %n", jsonResponse);
            return null;
        }
    }

    /**
     * Method that assigns url for their profile picture to a user
     */
    public static void updateProfilePicture() {
        try {
            URL url = new URL(Constants.updateIcon);
            String postdata = String.format("user_id=%d&icon_url=%s", Utils.loggedInUser.getId(), Utils.loggedInUser.getIcon());
            DBGeneral.sendHttpPostRequest(url, postdata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void editBio(String newBio) {
        try {
            URL url = new URL(Constants.updateUserBio);
            String postdata = String.format("user_id=%d&field=%s&new_field=%s",
                    Utils.loggedInUser.getId(), "bio",newBio);
            String response = DBGeneral.sendHttpPostRequest(url, postdata);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

