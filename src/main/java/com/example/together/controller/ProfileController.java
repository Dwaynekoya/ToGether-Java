package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBUsers;
import com.example.together.dboperations.PhotoUploader;
import com.example.together.model.Group;
import com.example.together.model.User;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController {
    public RadioButton radioUser, radioGroup;
    public Rectangle coverRectangle;
    public VBox searchBox;
    public TextField searchField;
    public ImageView profilePic;
    public Button searchGroup, searchFriend;
    public ListView listviewSearch;
    public Button createGroup;
    @FXML
    private Button settingsButton, groupButton, listButton, homeButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label bioLabel;
    private ToggleGroup toggleGroup;

    /**
     * Shows user info and sets up the sidebar buttons
     * Handles radio button logic
     * Sets the cell renderer for the listview
     */
    public void initialize() {
        usernameLabel.setText(Utils.loggedInUser.getUsername());
        bioLabel.setText(Utils.loggedInUser.getBio());
        profilePic.setImage(new Image(Utils.loggedInUser.getIcon()));
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
        //Radio buttons:
        toggleGroup= new ToggleGroup();
        radioGroup.setToggleGroup(toggleGroup);
        radioUser.setToggleGroup(toggleGroup);

        listviewSearch.setCellFactory(param -> new SearchListCell());
    }

    /**
     * Sends a LIKE query with the text in the textfield.
     * Table for the query depends on the selected radio button.
     * @param actionEvent search button
     */
    public void search(ActionEvent actionEvent) {
        Toggle selectedRadioButton = toggleGroup.getSelectedToggle();
        String search = searchField.getText().strip();
        if (selectedRadioButton.equals(radioGroup)){
            listviewSearch.setItems(DBGroup.searchGroups(search));
        } else if (selectedRadioButton.equals(radioUser)){
            listviewSearch.setItems(DBUsers.searchUsers(search));
        }
    }

    /**
     * Shows a hidden part of the view destined to search elements from the groups and users tables
     * @param actionEvent the search for more buttons.
     */
    public void searchView(ActionEvent actionEvent) {
        if (actionEvent.getSource() == searchGroup) {
            radioGroup.setSelected(true);
        } else if (actionEvent.getSource() == searchFriend) {
            radioUser.setSelected(true);
        }
        coverRectangle.setVisible(true);
        searchBox.setVisible(true);
    }

    /**
     * Switches view to create a new group.
     * @param actionEvent Create group button
     */
    public void newGroupView(ActionEvent actionEvent) {
        ViewSwitcher.switchView(View.NEWGROUP);
    }

    public void changeProfilePicture(ActionEvent actionEvent) {
        File selectedFile = Utils.imageFileChooser(actionEvent);
        Utils.loggedInUser.setIcon(selectedFile.getAbsolutePath());
        PhotoUploader photoUploader = new PhotoUploader(selectedFile,Utils.loggedInUser);
        photoUploader.start();
        DBUsers.updateProfilePicture(Utils.loggedInUser);
    }

    /**
     * Format for the cells in the listview. Includes a button next to each element.
     * Changes the action triggered depending on whether the search is for a group or an user.
     */
    private static class SearchListCell extends ListCell<Object> {
        private HBox content;
        private Button actionButton;
        public SearchListCell() {
            super();
            actionButton = new Button("Action");
            actionButton.getStyleClass().add("darkButton");

            content = new HBox();

            content.setAlignment(Pos.CENTER_LEFT);
            content.setSpacing(80);
            content.getChildren().add(actionButton);
        }
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                actionButton.setDisable(false);
                if (item instanceof User) {
                    User user = (User) item;
                    /*if (user.getId()==Utils.loggedInUser.getId()){
                        setGraphic(null);
                        setText(null);
                        actionButton.setDisable(true);
                    }else {
                        setText(user.getUsername());
                        actionButton.setText("Follow");
                        actionButton.setOnAction(event -> {
                            System.out.println("Button clicked for user: " + user.getUsername());
                            DBUsers.followUser(Utils.loggedInUser.getId(), user.getId());
                        });
                    }*/
                    setText(user.getUsername());
                    actionButton.setText("Follow");
                    actionButton.setOnAction(event -> {
                        System.out.println("Button clicked for user: " + user.getUsername());
                        DBUsers.followUser(Utils.loggedInUser.getId(), user.getId());
                    });
                    //can't follow someone you already follow
                    if (Utils.loggedInUser.getFollowing().contains(user)) actionButton.setDisable(true);
                } else if (item instanceof Group) {
                    Group group = (Group) item;
                    setText(group.getName());
                    actionButton.setText("Join");
                    actionButton.setOnAction(event -> {
                        System.out.println("Button clicked for group: " + group.getName());
                        DBGroup.putMember(group,Utils.loggedInUser);
                    });
                }
                setGraphic(content);
            }
        }
    }
}
