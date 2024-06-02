package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.util.*;

public class GroupsController {
    public Button settingsButton, groupButton, listButton, homeButton;
    //side profile:
    public Label usernameLabel;
    public ListView groupsListview, friendsListview;
    public Button openProfile;
    private ObservableSet<Task> tasksFromGroups = FXCollections.observableSet();
    private ObservableSet<Habit> habitsFromGroups = FXCollections.observableSet();
    public ScrollPane groupScrollPane;
    public VBox taskPopup;
    public void initialize() {
        Utils.sidebarSetup(settingsButton,groupButton,listButton,homeButton);
        Utils.profileSideSetup(usernameLabel,groupsListview,friendsListview, openProfile);
        fetchGroupTasks();
        displayGroups(Utils.loggedInUser.getGroups());
    }
    private void fetchGroupTasks(){
        for (Group group: Utils.loggedInUser.getGroups()){
            String json = DBGroup.getTasks(group.getId());
            tasksHabitsFromJSON(json, group);
        }
    }


    public void displayGroups(Set<Group> groups) {
        for (Group group : groups) {
            VBox groupVBox = new VBox();
            groupVBox.getStyleClass().add("groupbox"); // css class

            Label groupNameLabel = new Label(group.getName());

            ListView<Task> taskListView = new ListView<>();
            ListView<Habit> habitListView = new ListView<>();

            taskListView.setItems(FXCollections.observableArrayList(group.getSharedTasks()));
            habitListView.setItems(FXCollections.observableArrayList(group.getSharedHabits()));

            System.out.println("GROUPS: " + group.getSharedTasks());
            System.out.println("HABITS: " + group.getSharedHabits());

            HBox listViewsHBox = new HBox(taskListView, habitListView);

            Button addTask = new Button("Add task");
            addTask.setOnAction(actionEvent -> addGroupTask(group));
            addTask.getStyleClass().add("lightButton");
            HBox buttonContainer = new HBox(addTask);
            buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);

            groupVBox.getChildren().addAll(groupNameLabel, listViewsHBox, buttonContainer);

            groupScrollPane.setFitToWidth(true);
            groupScrollPane.setContent(groupVBox);
        }
    }

    private void addGroupTask(Group group) {
        Utils.groupNewTask = group;
        Utils.previousView = View.GROUPS;
        ViewSwitcher.switchView(View.NEWTASK);
    }

    public void habitToggle(ActionEvent actionEvent) {
    }

    public void editTask(ActionEvent actionEvent) {
    }

    public void closePopup(ActionEvent actionEvent) {
    }
    private void tasksHabitsFromJSON(String json, Group group) {
        if (json==null) return;

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

