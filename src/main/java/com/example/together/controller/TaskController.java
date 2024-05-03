package com.example.together.controller;

import com.example.together.dboperations.Constants;
import com.example.together.dboperations.DBTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TaskController {
    @FXML
    private TextField textfieldName;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea textAreaInfo;
    @FXML
    private CheckBox checkBox;
    private String taskName, date, info;
    private boolean habit= false;
    public void createTask(ActionEvent actionEvent) {
        takeInputData();
    }
//TODO: set max length on text fields
    private void takeInputData() {
        taskName = textfieldName.getText();
        info = textAreaInfo.getText();
        habit = checkBox.isSelected();
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.mysqlDateFormat);
            date = simpleDateFormat.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            // no date has been selected?
            date = null;
        }
        if (Utils.checkDataValidity(new ArrayList<>(Arrays.asList(taskName)))){
            addNewTask();
        } else {

        }
    }

    private void addNewTask() {
        //TODO: adding habit instead of task
        if (habit){
            System.out.printf("TODO");
            //TODO: close window/goback to home view
        } else {
            //TODO: use current user id
            DBTask.addTask(taskName,date,info,1);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        //TODO: Go back to home view
    }
}
