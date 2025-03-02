package com.example.demo.Services;

public class MyServices {
    public static String getUser(int id) {
        return "GET: User with ID " + id;
    }

    public static String createUser(String userData) {
        return  "POST:  create a user with user data  "+ userData;
    }

    public static String updateUser(int id, String updatedData) {
        return "UPDATE : updated the user with id" + id + "new data is" + updatedData;
    }

    public static String deleteUser(int id) {
        return "DELETE: User with ID " + id + " has been deleted.";
    }
}
