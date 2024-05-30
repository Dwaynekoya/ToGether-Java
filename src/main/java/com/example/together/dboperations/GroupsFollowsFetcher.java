package com.example.together.dboperations;

import com.example.together.controller.Utils;
import com.example.together.model.Group;
import com.example.together.model.User;

import java.util.HashSet;
import java.util.List;

public class GroupsFollowsFetcher extends Thread {
    User currentUser = Utils.loggedInUser;

    /**
     * Infinite loop to search for groups and users followed by the current user in the database
     */
    @Override
    public void run() {
        while (true){
            // Look for groups the current user belongs to
            HashSet<Group> groups = DBGroup.searchGroupsFromMember(currentUser);
            if (groups!=null) currentUser.setGroups(groups);
            // Look for users the current user follows
            HashSet<User> following = DBUsers.searchFollowing(currentUser);
            //System.out.println(following);
            if (following!=null) currentUser.setFollowing(following);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
