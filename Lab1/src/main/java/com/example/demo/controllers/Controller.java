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
        return userService.getUser(id);
    }

    @PostMapping
    public String createUser(@RequestBody String userData) {
        return userService.createUser(userData);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @RequestBody String updatedData) {
        return userService.updateUser(id, updatedData);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

}
