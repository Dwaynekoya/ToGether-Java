package com.example.together.view;

import com.example.together.controller.Utils;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.PhotoUploader;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDate;

/**
 * Custom ListCell implementation for displaying tasks with a CheckBox and a Button
 * Checkbox marks tasks as finished while the button deletes them
 */
public class TaskListCell<T extends Task> extends ListCell<T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        //don't show item if null
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            //checkbox to define task as finished or unfinished
            CheckBox checkBox = new CheckBox();
            boolean finished = item.isFinished();
            checkBox.setSelected(finished);
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    File selectedFile = Utils.imageFileChooser(event);

                    if (selectedFile != null) {
                        PhotoUploader photoUploader = new PhotoUploader(selectedFile, this.getItem());
                        photoUploader.start();
                        if (item instanceof Habit) {
                            LocalDate habitDate = item.getDate().toLocalDate().plusDays(((Habit) item).getRepetition());
                            Habit newHabit = new Habit((Habit) item, java.sql.Date.valueOf(habitDate));
                            DBTask.addTask(newHabit);
                        }
                        item.setFinished(true);
                        DBTask.updateTask(item);
                    }
                }
            });

            //label for the task name
            Label nameLabel = new Label(item.getName());
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(nameLabel, Priority.ALWAYS);

            //button that deletes a task from the list
            Button deleteButton = new Button("Delete");
            deleteButton.getStyleClass().add("darkButton"); //CSS
            deleteButton.setOnAction(event -> {
                getListView().getItems().remove(item);
                DBTask.deleteTask(item);
            });

            HBox hbox = new HBox(checkBox, nameLabel, deleteButton);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setSpacing(10);
            hbox.setFillHeight(true);

            setGraphic(hbox);
        }
    }
}