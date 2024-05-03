package com.example.together;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AddTest extends Application {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Create ListView to display tasks
        ListView<Task> listView = new ListView<>();
        listView.setItems(tasks);

        // Customize the cell factory to display each task with a CheckBox and a Button
        listView.setCellFactory(param -> new TaskListCell());

        // Add event handler to show task details in a popup window on double-click
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Task selectedTask = listView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    showTaskDetailsPopup(selectedTask, primaryStage);
                }
            }
        });

        // Create Button to add tasks
        Button addButton = new Button("Add Task");
        addButton.setOnAction(event -> {
            Task newTask = new Task("New Task", false); // Create a new task
            tasks.add(newTask); // Add the task to the list
        });

        // Create VBox to hold ListView and Button
        VBox root = new VBox(listView, addButton);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        // Set up stage
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setTitle("Task List");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Task class representing each task
    static class Task {
        private String name;
        private boolean completed;

        public Task(String name, boolean completed) {
            this.name = name;
            this.completed = completed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

    // Custom ListCell implementation for displaying tasks with a CheckBox and a Button
    static class TaskListCell extends javafx.scene.control.ListCell<Task> {
        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                CheckBox checkBox = new CheckBox(item.getName());
                checkBox.setSelected(item.isCompleted());
                checkBox.setOnAction(event -> item.setCompleted(checkBox.isSelected()));

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> getListView().getItems().remove(item));

                HBox hbox = new HBox(checkBox, deleteButton);
                hbox.setSpacing(10);

                setGraphic(hbox);
            }
        }
    }

    // Method to show task details in a popup window
    private void showTaskDetailsPopup(Task task, Window owner) {
        Stage popupStage = new Stage();
        popupStage.initOwner(owner);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Task Details");

        Label nameLabel = new Label("Name: " + task.getName());
        Label completedLabel = new Label("Completed: " + task.isCompleted());

        VBox popupRoot = new VBox(nameLabel, completedLabel);
        popupRoot.setPadding(new Insets(10));
        popupRoot.setSpacing(10);

        Scene scene = new Scene(popupRoot, 200, 100);
        popupStage.setScene(scene);
        popupStage.show();
    }
}
