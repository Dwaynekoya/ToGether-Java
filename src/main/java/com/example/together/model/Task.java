package com.example.together.model;

import java.sql.Date;

public class Task {
    private int id;
    private String name;
    private Date date;
    private String info;
    private boolean finished;
    private boolean shared;
    private String image; //image URL
    private User owner;
    /**
     * Constructor used to create a task from scratch
     * @param id -> identifier
     * @param name -> Name for the task
     * @param date -> Date it should get completed in
     * @param info -> additional information
     * @param finished -> has this been completed?
     * @param shared -> is this part of a group?
     * @param image -> url to an image (added when task is completed)
     */
    public Task(int id, String name, Date date, String info, boolean finished, boolean shared,String image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.info = info;
        this.finished = finished;
        this.shared = shared;
        this.image = image;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
