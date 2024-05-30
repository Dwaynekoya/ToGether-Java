package com.example.together.dboperations;

import com.example.together.controller.Utils;
import com.example.together.model.Habit;
import com.example.together.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DBTask {
    /**
     * Puts a new task in the tasks table (and, if it´s a habit, also in the habits one)
     * @param task new task to add to the table
     */
    public static void addTask(Task task) {
        try {
            URL url = new URL(Constants.addTask);
            StringBuilder postData = new StringBuilder();
            postData.append("name=").append(URLEncoder.encode(task.getName(), StandardCharsets.UTF_8));
            if (task.getDate() != null) postData.append("&date=").append(URLEncoder.encode(String.valueOf(task.getDate()), StandardCharsets.UTF_8));
            if (task.getInfo() != null) postData.append("&info=").append(URLEncoder.encode(task.getInfo(), StandardCharsets.UTF_8));
            postData.append("&user_id=").append(Utils.loggedInUser.getId());
            if (task instanceof Habit) postData.append("&repeat=").append(((Habit) task).getRepetition());
            //postData.append("&repeat=").append(repeat != null ? repeat : "null"); // Include repeat field

            DBGeneral.sendHttpPostRequest(url,postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTask(Task task) {
        System.out.println("Updating task with id " + task.getId());
        try {
            URL url = new URL(Constants.updateTasks);
            StringBuilder postData = new StringBuilder();
            postData.append("task_id=").append(task.getId())
                    .append("&name=").append(task.getName())
                    .append("&date=").append(task.getDate() != null ? task.getDate().toString() : "")
                    .append("&info=").append(task.getInfo());
            if (task instanceof Habit){
                //System.out.println("REPEAT: " + ((Habit) task).getRepetition());
                postData.append("&repeat=").append(((Habit) task).getRepetition());
            } /*else {
                postData.append("&repeat=").append("");
            }*/
            DBGeneral.sendHttpPostRequest(url,postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes task by its id
     * @param task Task to delete
     */
    public static void deleteTask(Task task) {
        int id = task.getId();
        System.out.println("Deleting task with id " + id);
        try {
            URL url = new URL(Constants.deleteTask);
            String postData = "id=" + id;
            String response = DBGeneral.sendHttpPostRequest(url,postData);
            System.out.println("Response when deleting: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets task as finished and adds the url for the assigned image
     * @param task to set as finished
     */
    public static void finishTask(Task task){
        try {
            URL url = new URL(Constants.finishTask);
            String queryString = String.format("task_id=%d&image=%s&finished=%s", task.getId(), task.getImage(), true);
            String response = DBGeneral.sendHttpPostRequest(url, queryString);
            System.out.printf("Response when finishing task with id $d: $s $n", task.getId(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all tasks from a given user from the database
     * @param userId current user identifying int
     * @param following if tasks are from a user that´s followed by the current user
     * @return StringBuilder json containing all tasks
     */
    public static String getTasks(int userId, boolean following) {
        try {
            URL url = new URL(Constants.getTasksFromUser + "?id=" + userId + "&following=" + following);
            return  DBGeneral.sendHttpGetRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
