package com.example.together.dboperations;

import com.example.together.controller.Utils;
import com.example.together.model.Group;
import com.example.together.model.User;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;

public class DBGroup {
    /**
     * Creates a new group in the database
     * @param newGroup object containing the name and description to be used
     */

    public static void createGroup(Group newGroup) {
        try {
            URL url = new URL(Constants.createGroup);
            String postData = String.format("name=%s&description=%s&manager_id=%d",
                    URLEncoder.encode(newGroup.getName(), StandardCharsets.UTF_8),
                    URLEncoder.encode(newGroup.getDescription(), StandardCharsets.UTF_8),
                    Utils.loggedInUser.getId());
            String response = DBGeneral.sendHttpPostRequest(url,postData);
            System.out.printf("Response when creating group: %s %n", response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds relationship between user and group
     * @param group group to add the user to
     * @param user user that will become a group member
     */
    public static void putMember(Group group, User user){
        try {
            URL url = new URL(Constants.putMember);
            String postdata = String.format("group_id=%d&user_id=%d", group.getId(), user.getId());
            String response = DBGeneral.sendHttpPostRequest(url, postdata);
            System.out.printf("Response when adding member to group: %s %n", response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks for groups with a LIKE query
     * @param name String to look for group names containing it
     * @return ObservableList with matches
     */
    public static ObservableList<Group> searchGroups(String name) {
        ObservableList<Group> groupList = FXCollections.observableArrayList();

        try {
            URL url = new URL(Constants.searchGroups);
            String postData =  "name=" + name;
            String jsonResponse = DBGeneral.sendHttpPostRequest(url, postData);

            Gson gson = new Gson();
            Type groupListType = new TypeToken<List<Group>>(){}.getType();
            List<Group> groups = gson.fromJson(jsonResponse, groupListType);
            groupList.addAll(groups);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return groupList;
    }

    /**
     * Looks for groups a member belongs to
     * @param user to extract id for the search
     * @return set of groups the user belongs to
     */
    public static HashSet<Group> searchGroupsFromMember(User user) {
        try {
            URL url = new URL(Constants.groupsFromMember + "?user_id=" + user.getId());
            String jsonResponse = DBGeneral.sendHttpGetRequest(url);
            if (jsonResponse.equals("")) return null;

            HashSet<Group> groupHashSet = new HashSet<>();
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();
            for (JsonElement jsonElement:jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int groupId = jsonObject.get("group_id").getAsInt();
                Group group = DBGroup.getGroup(groupId);
                if (group!=null) groupHashSet.add(group);
            }

            for (Group group: groupHashSet){
                group.setManager(DBGroup.searchManager(group));
            }

            return groupHashSet;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static User searchManager(Group group) {
        try {
            URL url = new URL(Constants.getManager);
            String postdata = String.format("group_id=%d", group.getId());
            String response = DBGeneral.sendHttpPostRequest(url, postdata);
            System.out.println(response);
            Gson gson = new Gson();
            User manager = gson.fromJson(response, User.class);
            return manager;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Looks for a group given its ID
     * @param groupId to identify the group
     * @return group object with data from the DB
     */
    private static Group getGroup(int groupId) {
        try {
            URL url = new URL(Constants.getGroupGivenId + "?id=" + groupId);
            String response = DBGeneral.sendHttpGetRequest(url);
            Gson gson = new Gson();
            Group group = gson.fromJson(response, Group.class);
            return group;
        } catch (IOException e) {
            System.out.println("Exception getting group from id");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all tasks associated with a group
     * @param id group identifier
     */
    public static String getTasks(int id) {
        try {
            URL url = new URL(Constants.getTasksFromGroup + "?id=" + id);
            return  DBGeneral.sendHttpGetRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

