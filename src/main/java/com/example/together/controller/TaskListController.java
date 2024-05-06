package com.example.together.controller;

import com.example.together.model.Task;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskListController implements Initializable {
    @FXML
    private ListView<Task> taskListView;
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    public void openProfile(ActionEvent actionEvent) throws Exception {
        ViewSwitcher.switchView(View.PROFILE);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO: load tasks from DB
        taskListView.setItems(tasks);
        taskListView.setCellFactory(param -> new TaskListCell());

        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    //TODO: open task view
                    System.out.println("Selected task");
                }
            }
        });
    }

    public void newTask(ActionEvent actionEvent) {
        ViewSwitcher.switchView(View.NEWTASK);
    }


    // Custom ListCell implementation for displaying tasks with a CheckBox and a Button
    static class TaskListCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(task.isFinished());
                checkBox.setOnAction(event -> task.setFinished(checkBox.isSelected()));

                Label nameLabel = new Label(task.getName());
                nameLabel.setAlignment(Pos.CENTER_LEFT);
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nameLabel, Priority.ALWAYS);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> getListView().getItems().remove(task));

                HBox hbox = new HBox(checkBox, nameLabel, deleteButton);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setSpacing(10);
                hbox.setFillHeight(true);

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
        Label completedLabel = new Label("Completed: " + task.isFinished());

        VBox popupRoot = new VBox(nameLabel, completedLabel);
        popupRoot.setPadding(new Insets(10));
        popupRoot.setSpacing(10);

        Scene scene = new Scene(popupRoot, 200, 100);
        popupStage.setScene(scene);
        popupStage.show();
    }
}
