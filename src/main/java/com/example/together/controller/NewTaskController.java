package com.example.together.controller;

import com.example.together.dboperations.Constants;
import com.example.together.dboperations.DBTask;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class NewTaskController implements Initializable {
    @FXML
    private TextField textfieldName;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea textAreaInfo;
    @FXML
    private CheckBox checkBox;
    @FXML
    private HBox habitbox;
    @FXML
    private Label labelRequiredFields;
    @FXML
    private Spinner spinnerRepeat;
    private String taskName, date, info;
    private boolean habit= false;
    public void createTask(ActionEvent actionEvent) {
        takeInputData();
    }
//TODO: set max length on text fields

    /**
     * Takes the data from the input fields present on the fxml view
     */
    private void takeInputData() {
        taskName = textfieldName.getText();
        info = textAreaInfo.getText();
        LocalDate selectedDate = datePicker.getValue();
        //if it doesn´t have a selected date, ignore the date field
        if (selectedDate != null && selectedDate.getYear() != 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.mysqlDateFormat);
            date = simpleDateFormat.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            // no date has been selected
            date = null;
        }
        if (Utils.checkDataValidity(new ArrayList<>(Arrays.asList(taskName)))){
            addNewTask();
        } else {
            labelRequiredFields.setVisible(true);
        }
    }

    /**
     * Adds a new task to database. The php file hosted decides whether to put it into the tasks or habits table.
     */
    private void addNewTask() {
        if (habit){
            DBTask.addTask(taskName,date,info,Utils.loggedInUser.getId(), (Integer) spinnerRepeat.getValue());
        } else {
            DBTask.addTask(taskName,date,info,Utils.loggedInUser.getId(), null);
        }
        ViewSwitcher.switchView(View.TASKLIST);
    }
    /**
     * Switches view back to the tasklists view
     * @param actionEvent  Cancel button
     */
    public void cancel(ActionEvent actionEvent) {
        ViewSwitcher.switchView(View.TASKLIST);
    }

    /**
     * Hides or shows the input fields required for creating a task
     * @param actionEvent Habit checkbox
     */
    public void habitToggle(ActionEvent actionEvent) {
        habit = !habit;
        habitbox.setVisible(habit);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        habit=false;
        spinnerRepeat.setEditable(true);
        spinnerRepeat.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinnerRepeat.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
