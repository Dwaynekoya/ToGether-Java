package com.example.together.dboperations;

import com.example.together.model.Task;
import com.example.together.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PhotoUploader extends Thread{
    private File photoFile;
    private Task task;
    private User user;
    public PhotoUploader (File photoFile, Task task) {
        this.photoFile=photoFile;
        this.task =task;
    }
    public PhotoUploader(File photoFile, User user){
        this.photoFile=photoFile;
        this.user=user;
    }

    /**
     * uploads a picture and sets its url as a field in a given object (either a Task or a User)
     */
    @Override
    public void run() {
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just a random string
        String CRLF = "\r\n"; // required by multipart/form-data

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Constants.uploadPhoto).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)
            ) {
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"").append(photoFile.getName()).append("\"").append(CRLF);
                writer.append("Content-Type: ").append(HttpURLConnection.guessContentTypeFromName(photoFile.getName())).append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();

                try (InputStream input = new FileInputStream(photoFile)) {
                    byte[] buffer = new byte[4096];
                    for (int n; (n = input.read(buffer)) != -1; ) {
                        output.write(buffer, 0, n);
                    }
                    output.flush();
                }

                writer.append(CRLF).flush();
                writer.append("--").append(boundary).append("--").append(CRLF).flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    if (jsonResponse.has("url")) {
                        String url = jsonResponse.get("url").getAsString();
                        System.out.println("URL FOR UPLOAD: " + url);
                        if (task!=null){
                            task.setImage(url);
                            //DBTask.updateTask(task);
                            task.setFinished(true);
                            DBTask.finishTask(task);
                        }
                        if (user!=null){
                            user.setIcon(url);
                            DBUsers.updateProfilePicture();
                        }
                    } else {
                        System.out.println("Failed to get URL from server response.");
                    }
                }
            } else {
                System.out.println("Failed to upload photo. Server returned response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
