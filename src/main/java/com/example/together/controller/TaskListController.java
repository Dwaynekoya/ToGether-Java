package com.example.together.controller;

import com.example.together.dboperations.Constants;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.PhotoUploader;
import com.example.together.dboperations.TaskFetcher;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.example.together.view.TaskListCell;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TaskListController {
    public Button settingsButton, groupButton, listButton, homeButton, openProfile; //buttons in sidebar + right menu

    @FXML
    private ListView<Task> taskListView;
    @FXML
    private ListView<Habit> habitListView;
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ObservableList<Habit> habits = FXCollections.observableArrayList();
    private Task selectedTask;
    //elements for popup view
    public VBox taskPopup;
    public TextField textfieldName;
    public DatePicker datePicker;
    public TextArea textAreaInfo;
    public CheckBox checkBox;
    public HBox habitbox;
    public Spinner spinnerHabit;
    private String taskName, date, info;
    private int repeat;
    //profile menu
    public Label usernameLabel;
    public ListView groupsListview;
    public ListView friendsListview;

    private boolean habit= false;

    /**
     * Switches view to the profile one
     * @param actionEvent profile button
     */
    public void openProfile(ActionEvent actionEvent)  {
        ViewSwitcher.switchView(View.PROFILE);
    }

    /**
     * Initializes the view setting up the listviews to represent data from the DB and setting their listeners
     * It also forces the spinner in the popup to edit Tasks to only accept integers
     */
    public void initialize() {
        TaskFetcher taskFetcher = new TaskFetcher(Utils.loggedInUser.getId(), tasks, habits);
        taskFetcher.start();

        listViewsSetup(taskListView, tasks);
        listViewsSetup(habitListView, habits);

        //set spinner so it only takes integers
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1);
        spinnerHabit.setValueFactory(valueFactory);

        spinnerHabit.setEditable(true);
        spinnerHabit.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinnerHabit.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
        Utils.profileSideSetup(usernameLabel,groupsListview,friendsListview,openProfile);
        Utils.setCharacterLimit(textfieldName, 60);
        Utils.setCharacterLimit(textAreaInfo, 600);
    }

    /**
     * Assigns items to listviews, gives them a custom look and adds a listener to show their items when double-clicked
     */
    private <T extends Task> void listViewsSetup(ListView<T> listView, ObservableList<T> tasks) {
        listView.setItems(tasks);
        listView.setCellFactory(param -> new TaskListCell<>());
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                T selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selectedTask = selectedItem;
                    showPopup();
                }
            }
        });
    }

    /**
     * Method that switches the view to the one for creating new tasks
     * @param actionEvent new task button
     */
    public void newTask(ActionEvent actionEvent) {
        ViewSwitcher.switchView(View.NEWTASK);
    }

    /**
     * Method that updates the database by using the values inputted by the user
     * @param actionEvent
     */
    public void editTask(ActionEvent actionEvent) {
        System.out.println("Selected task id: " + selectedTask.getId());
        Habit newHabit = new Habit(selectedTask,repeat);
        System.out.println("New id: " + newHabit.getId());
        takeInputData();
        //if task didn't use to be a Habit, but has been modified to become one:
        if (! (selectedTask instanceof Habit) && habit){
            DBTask.updateTask(new Habit(selectedTask, repeat));
        } else {
            DBTask.updateTask(selectedTask);
        }
        closePopup(actionEvent);
    }

    /**
     * Hides the popup used to show task details
     * @param actionEvent cancel button
     */
    public void closePopup(ActionEvent actionEvent) {
        taskPopup.setVisible(false);
    }

    /**
     * Makes habit true/false and hides/unhides the related fields accordingly
     * @param actionEvent habit? checkbox
     */
    public void habitToggle(ActionEvent actionEvent) {
        habit = !habit;
        habitbox.setVisible(habit);
    }
    /**
     * Shows popup populated with the values from the selected task on the list
     */
    private void showPopup() {
        textfieldName.setText(selectedTask.getName());
        textAreaInfo.setText(selectedTask.getInfo() != null ? selectedTask.getInfo() : "");
        if (selectedTask.getDate() != null) {
            datePicker.setValue(selectedTask.getDate().toLocalDate());
        }

        if (selectedTask instanceof Habit){
            habit = true;
            checkBox.setSelected(true);
            habitbox.setVisible(true);
            spinnerHabit.getValueFactory().setValue(((Habit) selectedTask).getRepetition());
        } else {
            habit = false;
            checkBox.setSelected(false);
            habitbox.setVisible(false);
            spinnerHabit.getValueFactory().setValue(0);
        }
        taskPopup.setVisible(true);
    }

    /**
     * Method that assigns values in user editable fields to variables
     */

    private void takeInputData() {
        taskName = textfieldName.getText();
        info = textAreaInfo.getText();
        LocalDate selectedDate = datePicker.getValue();
        //if it doesn´t have a selected date, ignore the date field
        if (selectedDate.getYear() != 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.mysqlDateFormat);
            date = simpleDateFormat.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            // no date has been selected
            date = null;
        }
        ArrayList<String> fieldsToFill;
        if (habit){
            repeat = (int) spinnerHabit.getValue();
            fieldsToFill = new ArrayList<>(Arrays.asList(taskName,repeat+""));
        } else {
            fieldsToFill = new ArrayList<>(Arrays.asList(taskName));
        }
        if (Utils.checkDataValidity(fieldsToFill)){
            assignValues();
        } else {
            //TODO: label
            //labelRequiredFields.setVisible(true);
        }
    }

    /**
     * Assigns the values that have been taken from the input fields by takeInputData() to the corresponding fields in the Task object
     */
    private void assignValues() {
        selectedTask.setName(taskName);
        selectedTask.setInfo(info);
        selectedTask.setDate(java.sql.Date.valueOf(date));
        if (selectedTask instanceof Habit) ((Habit) selectedTask).setRepetition(repeat);
    }
}
