package com.example.together;

import static com.example.together.dboperations.DBUsers.registerUser;

public class CargaDatosPrueba {
    public static void main(String[] args) {
        //REGISTRO USUARIO DE PRUEBA EN BBDD
        String username = "exampleUser";
        String password = "examplePassword";
        int insertedId = registerUser(username, password);
        System.out.println("Inserted ID: " + insertedId);
    }
}
