package com.example.together.dboperations;

import com.example.together.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DBUsers {
    /**
     * Looks for a given user in the table using a php file
     * @param username username that identifies the user to look for
     * @return -1 if not found, -2 if incorrect password. Id if found
     */
    public static int login(String username, String password) {
        int userId = -1;
        try {
            URL url = new URL(Constants.login);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            connection.getOutputStream().write(data.getBytes("UTF-8"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine(); // Read the response line
            reader.close();


            try {
                userId = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    private static String getUserBio(int userId) {
        //TODO: implement
        //if null, set a standard message
        return "";
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                generatedId = Integer.parseInt(line.trim());
            }
            reader.close();

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

}
