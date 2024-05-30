package com.example.together.controller;

import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Task;
import com.example.together.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedController {
    @FXML
    private Button settingsButton, groupButton, listButton, homeButton;
    private Set<Task> tasksFromFollowing=new HashSet<>();
    @FXML
    private ScrollPane scrollPane;

    public void initialize(){
        fetchFollowingTasks();
    }

    /**
     * Gets finished tasks from users you follow from DB
     */
    private void fetchFollowingTasks(){
        for (User user: Utils.loggedInUser.getFollowing()){
            String json = DBTask.getTasks(user.getId(), true);
            tasksFromFollowing.addAll(Utils.parseTasks(json));
        }
        populateScrollPaneWithTasks();
    }
    private void populateScrollPaneWithTasks() {
        VBox container = new VBox(); // Container VBox to hold all task VBoxes

        for (Task task : tasksFromFollowing) {
            VBox taskBox = new VBox();
            taskBox.getStyleClass().add("taskbox");


            ImageView imageView = new ImageView(new Image(task.getImage()));
            imageView.setFitWidth(400);
            imageView.setFitHeight(500);

            Label nameLabel = new Label(task.getName());
            Label descriptionLabel = new Label(task.getInfo());

            taskBox.getChildren().addAll(imageView, nameLabel, descriptionLabel);

            container.getChildren().add(taskBox);
        }

        scrollPane.setContent(container);
    }

}
