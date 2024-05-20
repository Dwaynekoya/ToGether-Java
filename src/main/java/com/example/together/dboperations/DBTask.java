package com.example.together.dboperations;

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
    public static void addTask(String name, String date, String info, int userId, Integer repeat) {
        try {
            URL url = new URL(Constants.addTask);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            StringBuilder postData = new StringBuilder();
            postData.append("name=").append(URLEncoder.encode(name, StandardCharsets.UTF_8));
            if (date != null) postData.append("&date=").append(URLEncoder.encode(date, StandardCharsets.UTF_8));
            if (info != null) postData.append("&info=").append(URLEncoder.encode(info, StandardCharsets.UTF_8));
            postData.append("&user_id=").append(userId);
            if (repeat!=null) postData.append("&repeat=").append(repeat);
            //postData.append("&repeat=").append(repeat != null ? repeat : "null"); // Include repeat field

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Task added successfully.");
            } else {
                System.out.println("Failed to add task. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTask(Task task) {
        try {
            URL url = new URL(Constants.updateTasks);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

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


            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postDataBytes);
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Task updated successfully.");
            } else {
                System.err.println("Failed to update task. HTTP response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(Task task) {
        int id = task.getId();
        System.out.println("Deleting task with id " + id);
        try {
            URL url = new URL(Constants.deleteTask);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "id=" + id;

            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postDataBytes);
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Task deleted successfully.");
            } else {
                System.err.println("Failed to delete task. HTTP response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
