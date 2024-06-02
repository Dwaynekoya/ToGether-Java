package com.example.together.controller;

import com.example.together.Main;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Set;

public class Utils {
    public static User loggedInUser;
    public static User selectedUser; //for popups
    public static Group selectedGroup; //for popups
    public static Group groupNewTask; //to add a new task for a group
    public static View previousView; //to go back to after closing add task view
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
        groupButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.GROUPS));
        listButton.setOnAction(actionEvent -> ViewSwitcher.switchView(View.TASKLIST));
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

        // Handle double-click on list view items to show a popup with additional info
        groupsListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    showPopup(View.GROUP_POPUP);
                }
            }
        });

        friendsListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                selectedUser = friendsListView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    showPopup(View.USER_POPUP);
                }
            }
        });
    }

    /**
     * Shows a fxml view as a popup window
     * @param view View to display (fxml file)
     */
    public static void showPopup(View view) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(view.getFileName()));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED); // Hide the title bar
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /**
     * Opens a file chooser that can only pick image files
     * @param event used to extract the current window
     * @return chosen image file
     */
    public static File imageFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // File chooser can only pick image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        Window currentWindow = ((ButtonBase) event.getSource()).getScene().getWindow();
        return fileChooser.showOpenDialog(currentWindow);
    }
    public void showPopup(Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/popup_view.fxml"));
            Parent root = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UNDECORATED);  // Hide the title bar
            popupStage.setScene(new Scene(root));

            // Set the popup at the center of the parent window
            popupStage.setX(parentStage.getX() + parentStage.getWidth() / 2 - root.prefWidth(-1) / 2);
            popupStage.setY(parentStage.getY() + parentStage.getHeight() / 2 - root.prefHeight(-1) / 2);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
