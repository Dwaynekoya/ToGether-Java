package com.example.together.dboperations;

import com.example.together.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Set;

public class DBUsers {
    /**
     * Looks for a given user in the table using a php file
     * @param username username that identifies the user to look for
     * @return -1 if not found, -2 if incorrect password. Id if found
     */
    public static int login(String username, String password) throws Exception {
        int userId = -1;
        URL url = new URL(Constants.login);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

        connection.getOutputStream().write(data.getBytes("UTF-8"));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = bufferedReader.readLine();
        bufferedReader.close();

        try {
            userId = Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        connection.disconnect();

        return userId;
    }

    /**
     * Puts a new user into the table using a php file.
     * @param username unique String to identify user
     * @param password
     * @return -1 if failed, -2 if username already exists. If succesful, new user id
     */
    public static int registerUser(String username, String password){
        int generatedId = -1;
        try {
            URL url = new URL(Constants.registerUser);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            connection.getOutputStream().write(data.getBytes("UTF-8"));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                generatedId = Integer.parseInt(line.trim());
            }
            bufferedReader.close();

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public static User getUser(int id) {
        try {
            String url = Constants.getUserGivenId + "?id=" + id;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            Gson gson = new Gson();
            User user = gson.fromJson(response.toString(), User.class);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Set<User> getAllUsers() throws IOException {
        URL url = new URL(Constants.getUsers);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        Type userType = new TypeToken<Set<User>>() {}.getType();
        Set<User> users = gson.fromJson(response.toString(), userType);

        return users;
    }
}
