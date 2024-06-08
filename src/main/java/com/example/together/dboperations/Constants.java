package com.example.together.dboperations;

import java.net.URL;

public class Constants {
    public static String phpLocation = "http://192.168.1.101/ToGether/";
    //USER MANAGEMENT PHP FILES:
    public static String updateUserBio = phpLocation + "phpusers/updateuser_bio.php";
    public static String getUserGivenId = phpLocation+"phpusers/getuser_id.php";
    public static String registerUser = phpLocation+"phpusers/putuser.php";
    public static String login = phpLocation+"phpusers/login.php";
    public static String getFollowing = phpLocation+ "phpfollows/getfollowing.php";
    public static String searchUsers = phpLocation + "phpusers/searchusers.php";
    public static String updateIcon = phpLocation + "phpusers/updateuser_icon.php";
    public static String editUser = phpLocation + "phpusers/edituser.php";
    //follows:
    public static String followUser= phpLocation + "phpfollows/followuser.php";
    //TASK MANAGEMENT PHP FILES:
    public static String addTask = phpLocation + "phptasks/puttask.php";
    public static String getTasksFromUser = phpLocation + "phptasks/gettasks_user.php";
    public static String uploadPhoto = phpLocation + "phptasks/uploadphoto.php";
    public static String updateTasks = phpLocation +"phptasks/edittasks.php";
    public static String deleteTask = phpLocation + "phptasks/deletetask.php";
    public static String finishTask = phpLocation + "phptasks/finishtask.php";
    public static String deleteAllTasks = phpLocation + "phptasks/deletealltasks.php";
    //GROUP MANAGEMENT PHP FILES
    public static String createGroup = phpLocation + "phpgroups/putgroup.php";
    public static String searchGroups = phpLocation + "phpgroups/searchgroups.php";
    public static String putMember = phpLocation + "phpgroups/putmember.php";
    public static String groupsFromMember = phpLocation + "phpgroups/getgroups_frommember.php";
    public static String getGroupGivenId = phpLocation + "phpgroups/getgroup.php";
    public static String getTasksFromGroup = phpLocation + "phpgroups/gettasks_group.php";
    public static String getManager = phpLocation + "phpgroups/getmanager.php";
    public static String leaveAllGroups = phpLocation + "phpgroups/leaveallgroups.php";
    public static String editGroup = phpLocation + "phpgroups/editgroup.php";
    public static String deleteGroup = phpLocation + "phpgroups/removegroup.php";
    //Patterns
    public static String mysqlDateFormat = "yyyy-MM-dd";

}
