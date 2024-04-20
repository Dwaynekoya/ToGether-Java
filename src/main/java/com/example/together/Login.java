package com.example.together;

import static com.example.together.dboperations.DBUsers.searchUsernameInDB;

public class Login {
    public static void main(String[] args) {

        // Replace "username_to_query" with the actual username you want to query
        String usernameToQuery = "username_to_query";
        searchUsernameInDB(usernameToQuery);
    }


}
