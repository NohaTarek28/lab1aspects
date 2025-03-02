package com.example.demo.user;

import com.example.demo.user.dto.CreateUserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Integer id) {
        return this.userRepository.findById(id);
    }

    // Create a new user (Fixed order of parameters)
    public User createUser(CreateUserDto userDto) {
        User user = new User(
                null, // ID should be auto-generated
                userDto.getEmail(),
                userDto.getUsername(),
                userDto.getPhoneNumber(), // Fixed: phoneNumber comes before password
                userDto.getPassword()
        );
        return this.userRepository.save(user);
    }

    // Update an existing user
    public User updateUser(Integer id, CreateUserDto userDto) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setPhoneNumber(userDto.getPhoneNumber());
            return userRepository.save(user);
        }).orElse(null);
    }

    // Delete user by ID
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
