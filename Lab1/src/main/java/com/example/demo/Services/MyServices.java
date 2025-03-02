package com.example.demo.Services;

import org.springframework.stereotype.Service;

@Service
public class MyServices {
    public  String getUser(int id) {
        return "GET: User with ID " + id;
    }

    public  String createUser(String userData) {
        return  "POST:  create a user with user data  "+ userData;
    }

    public  String updateUser(int id, String updatedData) {
        return "UPDATE : updated the user with id" + id + "new data is" + updatedData;
    }

    public  String deleteUser(int id) {
        return "DELETE: User with ID " + id + " has been deleted.";
    }
}
