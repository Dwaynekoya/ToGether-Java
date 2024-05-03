package com.example.together.dboperations;

import com.example.together.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DBTask {
    public static void addTask(String name, String date, String info, int userId) {
        try {
            URL url = new URL(Constants.addTask);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "name=" + name +
                    "&date=" + date +
                    "&info=" + info +
                    "&user_id=" + userId +
                    "finished=" + false;

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
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

    public static void main(String[] args) {
        //addTask("Task 1", "2024-04-28", "Task information", false, true, "image.jpg");
        addTask("Task 2", null, "Task information",1);
    }
    public static List<Task> parseTasksFromJson(String jsonString) {
        Gson gson = new GsonBuilder().setDateFormat(Constants.mysqlDateFormat).create();;
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(jsonString, taskListType);
        return tasks;
    }
}
