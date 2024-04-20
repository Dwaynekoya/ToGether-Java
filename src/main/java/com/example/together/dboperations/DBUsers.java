package com.example.together.dboperations;

import com.example.together.Constants;
import com.example.together.model.Group;
import com.example.together.model.Task;
import com.example.together.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class DBUsers {
    public static User searchUsernameInDB(String usernameToQuery) {
        User user = null;
        try {
            // Connection to DB
            URL url = new URL(Constants.getUserGivenUsername + "?username=" + usernameToQuery);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //interpreting json response:
            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                //TODO: añadir consultas para obtener tareas, grupos, seguidores y personas a las que sigue
                //Set<User> following = (Set<User>) jsonObject.get("following");
                //Set<User> followers = (Set<User>) jsonObject.get("followers");
                //Set<Task> tasks = (Set<Task>) jsonObject.get("tasks");
                //Set<Group> groups = (Set<Group>) jsonObject.get("groups");
                Set<User> following = new HashSet<>();
                Set<User> followers = new HashSet<>();
                Set<Task> tasks = new HashSet<>();
                Set<Group> groups = new HashSet<>();
                user = new User(id, username, password, following,followers,tasks,groups);
                System.out.println(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
