package com.example.together.dboperations;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class TaskFetcher extends Thread {
    private final int userId;
    private final ObservableList<Task> taskList;
    private final ObservableList<Habit> habitList;

    /**
     * Constructor
     * @param userId from the current user
     * @param taskList from the tasklist view controller, observablelist
     * @param habitList from the tasklist view controller, observablelist
     */
    public TaskFetcher(int userId, ObservableList<Task> taskList, ObservableList<Habit> habitList) {
        this.userId = userId;
        this.taskList = taskList;
        this.habitList = habitList;
    }

    @Override
    public void run() {
       while (true){
           fetchTasks();
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    /**
     * Takes tasks and habits from database, converts the json data to Java objects and stores them in the lists provided
     */
    private void fetchTasks() {
        String json = DBTask.getTasks(userId, false);

        if (json==null) return;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SQLDateAdapter()) //adapts mysql dates
                .registerTypeAdapter(boolean.class, new BooleanTypeAdapter()) //adapts mysql booleans (0,1) to java ones
                .create();
//        System.out.println(json);
        JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

        List<Task> fetchedTasks = gson.fromJson(jsonObject.getAsJsonArray("tasks"), new TypeToken<List<Task>>() {}.getType());
        List<Habit> fetchedHabits = gson.fromJson(jsonObject.getAsJsonArray("habits"), new TypeToken<List<Habit>>() {}.getType());

        //Manually checking which tasks are habits, so they can be shown in the habits list and not in the tasks
        Map<Integer, Habit> habitMap = new HashMap<>();
        for (Habit habit : fetchedHabits) {
            habitMap.put(habit.getId(), habit);
        }
        Iterator<Task> taskIterator = fetchedTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
//            System.out.println(task.getName() + "  " + task.isFinished());
            if (habitMap.containsKey(task.getId())) {
                Habit habit = habitMap.get(task.getId());
                //System.out.println("Matching Task and Habit ID: " + task.getId());

                // Update habit with matching task attributes
                habit.setName(task.getName());
                habit.setDate(task.getDate());
                habit.setInfo(task.getInfo());
                habit.setFinished(task.isFinished());

                taskIterator.remove();
            }
        }

        ObservableList<Task> newTaskList = FXCollections.observableArrayList(fetchedTasks);
        ObservableList<Habit> newHabitList = FXCollections.observableArrayList(fetchedHabits);

        //runLater is used so that the updates are handled on the JavaFX thread
        Platform.runLater(() -> {
            if (!taskList.equals(newTaskList)) {
                taskList.setAll(newTaskList);
            }

            if (!habitList.equals(newHabitList)) {
                habitList.setAll(newHabitList);
            }
        });
    }
}
