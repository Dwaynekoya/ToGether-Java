package com.example.together.controller;

import com.example.together.dboperations.Constants;
import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.example.together.view.TaskListCell;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class GroupsController {
    public Button settingsButton, groupButton, listButton, homeButton;
    //side profile:
    public Label usernameLabel;
    public ListView groupsListview, friendsListview;
    public Button openProfile;
    public TextField textfieldName;
    public DatePicker datePicker;
    public TextArea textAreaInfo;
    public CheckBox checkBox;
    public HBox habitbox;
    public Spinner spinnerHabit;
    private ObservableSet<Task> tasksFromGroups = FXCollections.observableSet();
    private ObservableSet<Habit> habitsFromGroups = FXCollections.observableSet();
    public ScrollPane groupScrollPane;
    public VBox taskPopup;
    private Task selectedTask;
    private boolean habit;
    private int repeat;
    private String taskName, date, info;

    public void initialize() {
        fetchGroupTasks();
        displayGroups(Utils.loggedInUser.getGroups());

        Utils.integerSpinner(spinnerHabit);

        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
        Utils.profileSideSetup(usernameLabel,groupsListview,friendsListview, openProfile);
    }
    private void fetchGroupTasks(){
        for (Group group: Utils.loggedInUser.getGroups()){
            String json = DBGroup.getTasks(group.getId());
            System.out.println(json);
            tasksHabitsFromJSON(json, group);
        }
    }

    public void displayGroups(Set<Group> groups) {
        VBox scrollPaneVbox = new VBox();
        for (Group group : groups) {
            VBox groupVBox = new VBox();
            groupVBox.getStyleClass().add("groupbox"); // css class

            Label groupNameLabel = new Label(group.getName());

            ListView<Task> taskListView = new ListView<>();
            ListView<Habit> habitListView = new ListView<>();

            listViewsSetup(taskListView, FXCollections.observableArrayList(group.getSharedTasks()));
            listViewsSetup(habitListView,FXCollections.observableArrayList(group.getSharedHabits()));

            HBox listViewsHBox = new HBox(taskListView, habitListView);
            listViewsHBox.setAlignment(Pos.CENTER);
            listViewsHBox.setSpacing(60);

            Button addTask = new Button("Add task");
            addTask.setOnAction(actionEvent -> addGroupTask(group));
            addTask.getStyleClass().add("lightButton");
            HBox buttonContainer = new HBox(addTask);
            buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);

            groupVBox.getChildren().addAll(groupNameLabel, listViewsHBox, buttonContainer);
            scrollPaneVbox.getChildren().add(groupVBox);
        }
        groupScrollPane.setFitToWidth(true);
        groupScrollPane.setContent(scrollPaneVbox);
    }

    private void addGroupTask(Group group) {
        Utils.groupNewTask = group;
        Utils.previousView = View.GROUPS;
        ViewSwitcher.switchView(View.NEWTASK);
    }

    public void habitToggle(ActionEvent actionEvent) {
        habit = !habit;
        habitbox.setVisible(habit);
    }

    public void editTask(ActionEvent actionEvent) {
        takeInputData();
        //if task didn't use to be a Habit, but has been modified to become one:
        if (! (selectedTask instanceof Habit) && habit){
            DBTask.updateTask(new Habit(selectedTask, repeat));
        } else {
            DBTask.updateTask(selectedTask);
        }
        closePopup(actionEvent);
    }

    private void takeInputData() {
        taskName = textfieldName.getText();
        info = textAreaInfo.getText();
        LocalDate selectedDate = datePicker.getValue();
        //if it doesn´t have a selected date, ignore the date field
        if (selectedDate.getYear() != 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.mysqlDateFormat);
            date = simpleDateFormat.format(java.util.Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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

    /**
     * Hides the popup used to show task details
     * @param actionEvent cancel button
     */
    public void closePopup(ActionEvent actionEvent) {
        taskPopup.setVisible(false);
    }
    /**
     * Assigns items to listviews, gives them a custom look and adds a listener to show their items when double-clicked
     */
    public <T extends Task> void listViewsSetup(ListView<T> listView, ObservableList<T> tasks) {
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
     * Extracts Tasks and Habits from a json String
     * @param json json containing the tasks
     * @param group group the tasks belong to
     */
    private void tasksHabitsFromJSON(String json, Group group) {
        if (json==null) return;
        System.out.println(json);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        Set<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<Set<Task>>() {}.getType());
        Set<Habit> fetchedHabits = gson.fromJson(jsonObject.getAsJsonArray("habits"), new TypeToken<Set<Habit>>() {}.getType());

        //Manually checking which tasks are habits, so they can be shown in the habits list and not in the tasks
        Map<Integer, Habit> habitMap = new HashMap<>();
        for (Habit habit : fetchedHabits) {
            habitMap.put(habit.getId(), habit);
        }
        Iterator<Task> taskIterator = fetchedTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            if (habitMap.containsKey(task.getId())) {
                Habit habit = habitMap.get(task.getId());
                //System.out.println("Matching Task and Habit ID: " + task.getId());

                // Update habit with matching task attributes
                habit.setName(task.getName());
                habit.setDate(task.getDate());
                habit.setInfo(task.getInfo());

                taskIterator.remove();
            }
        }

        group.setSharedTasks(new HashSet<>(fetchedTasks));
        group.setSharedHabits(new HashSet<>(fetchedHabits));

        ObservableSet<Task> newTaskSet = FXCollections.observableSet(fetchedTasks);
        ObservableSet<Habit> newHabitSet = FXCollections.observableSet(fetchedHabits);

        //runLater is used so that the updates are handled on the JavaFX thread

        if (!tasksFromGroups.equals(newTaskSet)) {
            tasksFromGroups.addAll(newTaskSet);
        }

        if (!habitsFromGroups.equals(newHabitSet)) {
            habitsFromGroups.addAll(newHabitSet);
        }
    }
}

