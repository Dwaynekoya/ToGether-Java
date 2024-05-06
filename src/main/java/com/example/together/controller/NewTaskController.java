package com.example.together.controller;

import com.example.together.dboperations.Constants;
import com.example.together.dboperations.DBTask;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class NewTaskController {
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
    private String taskName, date, info;
    private boolean habit= false;
    public void createTask(ActionEvent actionEvent) {
        takeInputData();
    }
//TODO: set max length on text fields
    private void takeInputData() {
        taskName = textfieldName.getText();
        info = textAreaInfo.getText();
        //habit = checkBox.isSelected();
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
            labelRequiredFields.setVisible(true);
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
        ViewSwitcher.switchView(View.TASKLIST);
    }

    public void habitToggle(ActionEvent actionEvent) {
        habit = !habit;
        habitbox.setVisible(habit);
    }


}
