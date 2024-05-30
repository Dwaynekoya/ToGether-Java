package com.example.together.controller;

import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Task;
import com.example.together.model.User;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.util.List;
import java.util.Set;

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

    /**
     * Sets listeners for the buttons on the left side of most views
     * @param settingsButton opens the settings menu
     * @param groupButton shows the group view
     * @param listButton shows the tasklist view
     * @param homeButton shows the main feed
     */
    public static void sidebarSetup(Button settingsButton, Button groupButton, Button listButton, Button homeButton) {
        //TODO: Settings
        //settingsButton.setOnAction(actionEvent -> ViewSwitcher.switchView());
        //TODO: Social view
        //groupButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.GROUPS));
        listButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.TASKLIST));
        //TODO:Feed
        homeButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.FEED));
    }

    /**
     * Shows user info on the right side of some views
     * @param usernameLabel shows username
     * @param groupsListView shows groups the user belongs to
     * @param friendsListView shows users the user follows
     * @param openProfile shows profile view when clicked
     */
    public static void profileSideSetup(Label usernameLabel, ListView<Group> groupsListView, ListView<User> friendsListView, Button openProfile) {
        usernameLabel.setText(loggedInUser.getUsername());
        Set<Group> userGroups = loggedInUser.getGroups();
        Set<User> following = loggedInUser.getFollowing();

        if (userGroups != null) {
            ObservableList<Group> groupsObservableList = FXCollections.observableArrayList(userGroups);
            groupsListView.setItems(groupsObservableList);
        }

        if (following != null) {
            ObservableList<User> friendsObservableList = FXCollections.observableArrayList(following);
            friendsListView.setItems(friendsObservableList);
        }

        openProfile.setOnAction(event -> openProfile());

        // Handle double-click on groups list view items
        groupsListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Group selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    showGroupDetails(selectedGroup);
                }
            }
        });

        // Handle double-click on friends list view items
        friendsListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                User selectedUser = friendsListView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    showUserDetails(selectedUser);
                }
            }
        });
    }

    private static void showGroupDetails(Group group) {
        Label nameLabel = new Label(group.getName());
        Label descriptionLabel = new Label(group.getDescription());
        ListView<User> membersListView = new ListView<>();
        ObservableList<User> members = FXCollections.observableArrayList(group.getMembers()); // Assuming Group has a getMembers() method
        membersListView.setItems(members);
        ScrollPane membersScrollPane = new ScrollPane(membersListView);

        VBox groupDetailsBox = new VBox(nameLabel, descriptionLabel, membersScrollPane);
        groupDetailsBox.getStyleClass().add("taskbox");

        // TODO: Display the VBox
    }

    private static void showUserDetails(User user) {
        Label usernameLabel = new Label(user.getUsername());
        Label bioLabel = new Label(user.getBio());
        //TODO: profile pic in users
        ImageView profileImageView = new ImageView();
        //ImageView profileImageView = new ImageView(new Image(user.get()));
        profileImageView.setFitWidth(100);
        profileImageView.setFitHeight(100);
        profileImageView.setPreserveRatio(true);

        VBox userDetailsBox = new VBox(usernameLabel, bioLabel, profileImageView);
        userDetailsBox.getStyleClass().add("taskbox");

        // TODO: Display the VBox
    }

    /**
     * Switches view to the  current user's profile
     */
    public static void openProfile()  {
        ViewSwitcher.switchView(View.PROFILE);
    }

    /**
     * Sets a character limit in a given text input
     * @param textInputControl textfield/textarea to add the limit to
     * @param limit number of max. characters
     */
    public static void setCharacterLimit(TextInputControl textInputControl, int limit) {
        TextFormatter<String> textFormatter = new TextFormatter<>(change ->
                change.getControlNewText().length() <= limit ? change : null);
        textInputControl.setTextFormatter(textFormatter);
    }

    /**
     * Transforms a json into a list of Task objects
     * @param json received from the DB
     * @return list of fetched tasks
     */
    public static List<Task> parseTasks(String json) {
        if (json==null) return null;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();
        //System.out.println(json);
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        List<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<List<Task>>() {}.getType());
        return fetchedTasks;
    }

}
