package com.example.together.controller;

import com.example.together.dboperations.Constants;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.PhotoUploader;
import com.example.together.dboperations.TaskFetcher;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class TaskListController implements Initializable {
    public Button settingsButton;
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

    private boolean habit= false;
    public void openProfile(ActionEvent actionEvent) throws Exception {
        ViewSwitcher.switchView(View.PROFILE);
    }

    /**
     * Initializes the view setting up the listviews to represent data from the DB and setting their listeners
     * It also forces the spinner in the popup to edit Tasks to only accept integers
     * @param url needed for the override
     * @param resourceBundle needed for the override
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TaskFetcher taskFetcher = new TaskFetcher(Utils.loggedInUser.getId(), tasks, habits);
        taskFetcher.start();

        taskListView.setItems(tasks);
        taskListView.setCellFactory(param -> new TaskListCell());

        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Task selectedTaskFromList = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTaskFromList != null) {
                    selectedTask = selectedTaskFromList;
                    showPopup();
                    System.out.println("Selected task");
                }
            }
        });

        habitListView.setItems(habits);
        habitListView.setCellFactory(param -> new HabitCellList());

        habitListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
                if (selectedHabit != null) {
                    selectedTask = selectedHabit;
                    showPopup();
                    System.out.println("Selected habit");
                }
            }
        });

        //set spinner so it only takes integers
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1);
        spinnerHabit.setValueFactory(valueFactory);

        spinnerHabit.setEditable(true);
        spinnerHabit.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinnerHabit.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
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
        takeInputData();
        DBTask.updateTask(selectedTask);
        closePopup(actionEvent);
    }

    /**
     * Hides the popup used to show task details
     * @param actionEvent cancel button
     */
    public void closePopup(ActionEvent actionEvent) {
        taskPopup.setVisible(false);
    }

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
    //TODO: set max length on text fields
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
        System.out.println("New name: " + taskName);
        selectedTask.setName(taskName);
        selectedTask.setInfo(info);
        selectedTask.setDate(java.sql.Date.valueOf(date));
        if (selectedTask instanceof Habit) ((Habit) selectedTask).setRepetition(repeat);
        //DBTask.updateTask(selectedTask);
    }

    /**
     * Custom ListCell implementation for displaying tasks with a CheckBox and a Button
     * Checkbox marks tasks as finished while the button deletes them
     */
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
                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Select Image File");

                        // File chooser can only pick image files
                        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
                        fileChooser.getExtensionFilters().add(imageFilter);

                        Window currentWindow = ((CheckBox) event.getSource()).getScene().getWindow();

                        File selectedFile = fileChooser.showOpenDialog(currentWindow);

                        if (selectedFile != null) {
                            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                            // TODO: Add file upload
                            PhotoUploader photoUploader = new PhotoUploader(selectedFile, this.getItem());
                            photoUploader.run();
                            task.setFinished(true);
                        }
                    }
                });

                Label nameLabel = new Label(task.getName());
                nameLabel.setAlignment(Pos.CENTER_LEFT);
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nameLabel, Priority.ALWAYS);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> {
                    getListView().getItems().remove(task);
                    DBTask.deleteTask(task);
                });

                HBox hbox = new HBox(checkBox, nameLabel, deleteButton);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setSpacing(10);
                hbox.setFillHeight(true);

                setGraphic(hbox);
            }
        }
    }

    /**
     * Same list cell implementation for the Habit class
     * When checkbox is marked, creates a habit according to the repetition field in the completed one
     */
    static class HabitCellList extends ListCell<Habit> {
        @Override
        protected void updateItem(Habit habit, boolean empty) {
            super.updateItem(habit, empty);
            if (empty || habit == null) {
                setText(null);
                setGraphic(null);
            } else {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(habit.isFinished());
                checkBox.setOnAction(event -> {
                    //TODO: ADD PHOTO UPLOAD
                    Habit newRepetition = new Habit(habit,habit.getRepetition());
                    LocalDate habitDate = habit.getDate().toLocalDate().plusDays(habit.getRepetition());
                    newRepetition.setDate(java.sql.Date.valueOf(habitDate));
                    DBTask.addTask(habit.getName(), habit.getDate().toString(), habit.getInfo(), Utils.loggedInUser.getId(), habit.getId());
                    habit.setFinished(checkBox.isSelected());
                    DBTask.updateTask(habit);
                });

                Label nameLabel = new Label(habit.getName());
                nameLabel.setAlignment(Pos.CENTER_LEFT);
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nameLabel, Priority.ALWAYS);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> {
                    getListView().getItems().remove(habit);
                    DBTask.deleteTask(habit);
                });

                HBox hbox = new HBox(checkBox, nameLabel, deleteButton);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setSpacing(10);
                hbox.setFillHeight(true);

                setGraphic(hbox);
            }
        }
    }
}
