package com.example.together.dboperations;

public class Constants {
    public static String phpLocation = "http://localhost/ToGether/";
    //USER MANAGEMENT PHP FILES:
    public static String getUserGivenUsername = phpLocation+"phpusers/getuser_username.php";
    public static String getUserGivenId = phpLocation+"phpusers/getuser_id.php";
    public static String registerUser = phpLocation+"phpusers/putuser.php";
    public static String login = phpLocation+"phpusers/login.php";
    public static String getUsers = phpLocation + "phpusers/getusers.php";
    //TASK MANAGEMENT PHP FILES:
    public static String addTask = phpLocation + "phptasks/puttask.php";
    public static String getTasksFromUser = phpLocation + "phptasks/gettasks_user.php";
    public static String uploadPhoto = phpLocation + "phptasks/uploadphoto.php";
    public static String updateTasks = phpLocation +"phptasks/edittasks.php";
    public static String deleteTask = phpLocation + "phptasks/deletetask.php";
    public static String finishTask = phpLocation + "phptasks/finishtask.php";
    //Patterns
    public static String mysqlDateFormat = "yyyy-MM-dd";


}
