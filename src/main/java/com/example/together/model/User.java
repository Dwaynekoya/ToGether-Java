package com.example.together.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private int id;
    private String username;
    private String password;
    private String bio;
    private Set<User> following; //people this user follows
    private Set<User> followers; //people who follow this user
    private Set<Task> tasks;
    private Set<Group> groups;

    /**
     *
     * @param id -> Unique. Key.
     * @param username -> Unique, used for searching user
     * @param password -> Must contain alphanumeric characters
     * @param following -> pass empty set if empty. People whose tasks this user will see
     * @param followers -> pass empty set if empty. People who will see this user's tasks.
     * @param tasks -> pass empty set if empty.Tasks this user can manage.
     * @param groups -> pass empty set if empty. Groups this user belongs to
     */

    public User(int id, String username, String password, String bio, Set<User> following, Set<User> followers, Set<Task> tasks, Set<Group> groups) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.following = following;
        this.followers = followers;
        this.tasks = tasks;
        this.groups = groups;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.tasks = new HashSet<>();
        this.groups = new HashSet<>();
    }
    public User(int id, String username, String password, String bio) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.tasks = new HashSet<>();
        this.groups = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return username;
    }

    /**
     *
     * @param obj -> should be instaceof User. Object to be compared.
     * @return true if obj is a User object AND ids match
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        return this.id==((User) obj).id;
    }

    /**
     * Checks if user could login with the inputted data
     * @param user -> Object passed to compare
     * @return true if username AND password match.
     */
    public boolean login(User user){
        if (!this.username.equals(user.getUsername())) return false;
        return this.password.equals(user.getPassword());
    }
    public void follow(User user){
        this.following.add(user);
    }
    public void beFollowed(User user){
        this.followers.add(user);
    }
}
