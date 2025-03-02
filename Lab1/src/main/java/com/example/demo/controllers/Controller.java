package com.example.demo.controllers;

import com.example.demo.Services.MyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class Controller {
    private final MyServices userService;

    @Autowired
    public Controller(MyServices userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable int id) {
        return MyServices.getUser(id);
    }

    @PostMapping
    public String createUser(@RequestBody String userData) {
        return MyServices.createUser(userData);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @RequestBody String updatedData) {
        return MyServices.updateUser(id, updatedData);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        return MyServices.deleteUser(id);
    }

}
