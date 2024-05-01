package com.example.together.dboperations;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

            // Send POST request
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
        addTask("Task 2", "2024-05-01", "Task information",1);
    }
}
