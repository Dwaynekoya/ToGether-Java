package com.example.together.dboperations;

public class Constants {
    public static String phpLocation = "http://localhost/ToGether/";
    //USER MANAGEMENT PHP FILES:
    public static String getUserGivenUsername = phpLocation+"phpusers/getuser_username.php";
    public static String getUserGivenId = phpLocation+"phpusers/getuser_id.php";
    public static String registerUser = phpLocation+"phpusers/putuser.php";
    public static String login = phpLocation+"phpusers/login.php";
    //TASK MANAGEMENT PHP FILES:
    public static String addTask = phpLocation + "phptasks/puttask.php";
    //Patterns
    public static String mysqlDateFormat = "yyyy-MM-dd";
}
