package com.example.together.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private int id;
    private String username;
    private String password;
    private String bio;
    private String icon;
    private Set<User> following; //people this user follows
    private Set<User> followers; //people who follow this user
    private Set<Task> tasks;
    private Set<Group> groups;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return username;
    }

    /**
     * Compares users based on IDs
     * @param obj -> should be instaceof User. Object to be compared.
     * @return true if obj is a User object AND ids match
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        return this.id==((User) obj).id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
