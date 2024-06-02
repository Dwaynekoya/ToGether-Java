package com.example.together.model;

import java.util.Objects;
import java.util.Set;

public class Group {
    private int id;
    private String name;
    private String description;
    private Set<User> members;
    private Set<Task> sharedTasks;
    private Set<Habit> sharedHabits;
    private User manager;

    /**
     *
     * @param id ->  unique ID, used to join groups
     * @param name -> Short name to identify group
     * @param description -> Added by users to define the group
     * @param members -> All users that have access to group
     * @param sharedTasks -> if there are no tasks, pass empty set. Tasks shared between users
     */
    /*public Group(int id, String name, String description, Set<User> members, Set<Task> sharedTasks, User manager) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.members = members;
        this.sharedTasks = sharedTasks;
        this.manager=manager;
    }*/

    /**
     * Constructor used when creating a new group
     * @param name name for the group to be searched by
     * @param description added by users to define the group
     */
    public Group(String name, String description){
        this.name=name;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Task> getSharedTasks() {
        return sharedTasks;
    }

    public void setSharedTasks(Set<Task> sharedTasks) {
        this.sharedTasks = sharedTasks;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
    public void addSharedTask(Task task){
        this.sharedTasks.add(task);
    }

    public Set<Habit> getSharedHabits() {
        return sharedHabits;
    }

    public void setSharedHabits(Set<Habit> sharedHabits) {
        this.sharedHabits = sharedHabits;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Compares groups based on IDs
     * @param o should be instance of Group. Object to be compared to this group
     * @return true if obj is a Group with this id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
