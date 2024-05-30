package com.example.together.controller;

import com.example.together.dboperations.DBGroup;
import com.example.together.dboperations.DBTask;
import com.example.together.dboperations.GroupsFollowsFetcher;
import com.example.together.dboperations.SQLDateAdapter;
import com.example.together.model.Group;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.sql.Date;
import java.util.*;

public class GroupsController {
    private ObservableSet<Task> tasksFromGroups = FXCollections.observableSet();
    private ObservableSet<Habit> tasksFromHabits = FXCollections.observableSet();
    public void initialize() {
        fetchGroupTasks();
    }
    private void fetchGroupTasks(){
        for (Group group: Utils.loggedInUser.getGroups()){
            String json = DBGroup.getTasks(group.getId());
            tasksHabitsFromJSON(json);
        }
    }

    private void tasksHabitsFromJSON(String json) {
        if (json==null) return;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter())
                .create();
        //System.out.println(json);
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

        ObservableSet<Task> newTaskSet = FXCollections.observableSet(fetchedTasks);
        ObservableSet<Habit> newHabitSet = FXCollections.observableSet(fetchedHabits);

        //runLater is used so that the updates are handled on the JavaFX thread

        if (!tasksFromGroups.equals(newTaskSet)) {
            tasksFromGroups.addAll(newTaskSet);
            }

            if (!tasksFromHabits.equals(newHabitSet)) {
                tasksFromHabits.addAll(newHabitSet);
            }
        }
    }

