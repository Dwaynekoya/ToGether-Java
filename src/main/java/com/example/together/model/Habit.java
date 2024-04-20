package com.example.together.model;

import java.sql.Date;

public class Habit extends Task{
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
        this.repetition = repetition;
    }

    /**
     * Constructor used for converting a given task into a habit
     * @param task -> task to get attributes from
     * @param repetition -> value that turns task to HABIT
     */
    public Habit(Task task, int repetition){
        super(task.getId(),task.getName(),task.getDate(), task.getInfo(), task.isFinished(), task.isShared(), task.getImage());
        this.repetition = repetition;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }
}
