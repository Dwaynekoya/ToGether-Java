package com.example.together.controller;

import com.example.together.dboperations.DBUsers;
import com.example.together.model.Group;
import com.example.together.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

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
        //TODO: load user info
        usernameLabel.setText(Utils.loggedInUser.getUsername());
        bioLabel.setText(Utils.loggedInUser.getBio());
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
        //Radio buttons:
        toggleGroup= new ToggleGroup();
        radioGroup.setToggleGroup(toggleGroup);
        radioUser.setToggleGroup(toggleGroup);

        listviewSearch.setCellFactory(param -> new SearchListCell());
    }

    public void search(ActionEvent actionEvent) {
        Toggle selectedRadioButton = toggleGroup.getSelectedToggle();
        String search = searchField.getText().strip();
        if (selectedRadioButton.equals(radioGroup)){

        } else if (selectedRadioButton.equals(radioUser)){
            listviewSearch.setItems(DBUsers.searchUsers(search));
        }
    }

    public void searchView(ActionEvent actionEvent) {
        if (actionEvent.getSource() == searchGroup) {
            radioGroup.setSelected(true);
        } else if (actionEvent.getSource() == searchFriend) {
            radioUser.setSelected(true);
        }
        coverRectangle.setVisible(true);
        searchBox.setVisible(true);
    }
    private static class SearchListCell extends ListCell<Object> {
        private HBox content;
        private Button actionButton;
        private Label label;
        public SearchListCell() {
            super();
            label = new Label();
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
                    if (user.getId()==Utils.loggedInUser.getId()){
                        setGraphic(null);
                        setText(null);
                        actionButton.setDisable(true);
                    }else {

                        setText(user.getUsername());
                        actionButton.setText("Follow");
                        //TODO: Follow
                        actionButton.setOnAction(event -> {
                            System.out.println("Button clicked for user: " + user.getUsername());
                            DBUsers.followUser(Utils.loggedInUser.getId(), user.getId());
                        });
                    }
                } else if (item instanceof Group) {
                    Group group = (Group) item;
                    setText(group.getName());
                    actionButton.setText("Join");
                   //TODO: join group
                    actionButton.setOnAction(event -> {
                        System.out.println("Button clicked for group: " + group.getName());
                    });
                }
                setGraphic(content);
            }
        }
    }
}
