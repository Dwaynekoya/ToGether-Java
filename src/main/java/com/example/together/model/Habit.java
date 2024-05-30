package com.example.together.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class Habit extends Task{
    @SerializedName("task_id")
    private int id;
    private int repetition;

    /**
     * Constructor used to create a habit from scratch
     * @param id -> identifier
     * @param name -> Name for the task
     * @param date -> Date it should get completed in
     * @param info -> additional information
     * @param finished -> has this been completed?
     * @param shared -> is this part of a group?
     * @param image -> url to an image (added when task is completed)
     * @param  repetition-> number of days between each reocurrance of this task
     */

    public Habit(int id, String name, Date date, String info, boolean finished, boolean shared, String image, int repetition) {
        super(id, name, date, info, finished, shared, image);
        this.setId(id);
        this.repetition = repetition;
    }

    /**
     * Constructor used for converting a given task into a habit
     * @param task -> task to get attributes from
     * @param repetition -> value that turns task to HABIT
     */
    public Habit(Task task, int repetition){
        super(task.getId(),task.getName(),task.getDate(), task.getInfo(), task.isFinished(), task.isShared(), task.getImage());
        this.setId(task.getId()); //need to force this or else it gets assigned 0
        this.repetition = repetition;
    }

    /**
     * Used when completing a previous iteration of the habit
     * @param habit old iteration
     * @param habitDate new date to complete the habit by then
     */
    public Habit(Habit habit, Date habitDate) {
        super(habit.getId(), habit.getName(), habitDate, habit.getInfo(),false, habit.isShared(), habit.getImage());
        this.repetition = habit.getRepetition();
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
