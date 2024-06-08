package com.example.together.controller;

import com.example.together.dboperations.DBTask;

import com.example.together.model.Group;
import com.example.together.model.Task;
import com.example.together.model.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedController {
    public Label usernameLabel;
    public Button openProfile;
    public ListView<Group> groupsListview;
    public ListView<User> friendsListview;
    @FXML
    private Button settingsButton, groupButton, listButton, homeButton;
    private final Set<Task> tasksFromFollowing=new HashSet<>();
    @FXML
    private ScrollPane scrollPane;

    /**
     * Sets up sidebar and profile menu
     * Gets tasks from DB and adds them to the scrollpane in the middle of the pane
     */
    public void initialize(){
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
        Utils.profileSideSetup(usernameLabel,groupsListview,friendsListview,openProfile);

        fetchFollowingTasks();
        populateScrollPaneWithTasks();
    }

    /**
     * Gets finished tasks from users you follow from DB
     */
    private void fetchFollowingTasks(){
        for (User user: Utils.loggedInUser.getFollowing()){
            String json = DBTask.getTasks(user.getId(), true);
            List<Task> tasksFromUser = Utils.parseTasks(json);
            assert tasksFromUser != null;
            for (Task task: tasksFromUser) task.setOwner(user);
            tasksFromFollowing.addAll(tasksFromUser);
        }
    }

    /**
     * Goes over each task and prepares a container to show its info
     */
    private void populateScrollPaneWithTasks() {
        VBox container = new VBox(); // Container VBox to hold all task VBoxes

        if (tasksFromFollowing.isEmpty()) {
            container.getChildren().add(new Label("No one has finished their tasks yet..."));
            return;
        }

        for (Task task : tasksFromFollowing) {
            HBox taskBox = new HBox();
            taskBox.getStyleClass().add("taskbox");

            String imageURL = task.getImage();
            if (imageURL==null) return;
            ImageView imageView = new ImageView(new Image(task.getImage()));
            imageView.setFitWidth(240);
            imageView.setFitHeight(240);
            imageView.setPreserveRatio(true);
            //rounding the imageview's corners
            Rectangle clip = new Rectangle(
                    imageView.getFitWidth(), imageView.getFitHeight()
            );
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageView.setClip(clip);

            Label userLabel = new Label(task.getOwner().toString());
            VBox.setMargin(userLabel, new Insets(0,0,10,0));
            userLabel.getStyleClass().add("username-label");
            Label nameLabel = new Label(task.getName());
            Label descriptionLabel = new Label(task.getInfo());
            VBox labelVBox = new VBox(userLabel, nameLabel,descriptionLabel);
            HBox.setHgrow(labelVBox, Priority.ALWAYS);
            labelVBox.setAlignment(Pos.TOP_RIGHT);
            HBox.setMargin(labelVBox, new Insets(0, 20,0,20));

            taskBox.getChildren().addAll(imageView, labelVBox);

            VBox.setMargin(taskBox, new Insets(20));
            container.getChildren().add(taskBox);
        }

        scrollPane.setFitToWidth(true);
        scrollPane.setContent(container);
    }

}
