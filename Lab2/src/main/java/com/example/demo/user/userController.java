package com.example.demo.user;

import com.example.demo.user.dto.CreateUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/users")
public class userController {
    private final UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return this.userService.getUserById(id);
    }

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody CreateUserDto createUserDto) {
        return this.userService.createUser(createUserDto);
    }

    // Update user by ID
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody CreateUserDto createUserDto) {
        return this.userService.updateUser(id, createUserDto);
    }

    // Delete user by ID

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }

}
