package com.example.sqlconnect.controller;//package com.example.sqlconnect.controller;

import com.example.sqlconnect.model.classes.requests.UserRequest;
import com.example.sqlconnect.model.classes.response.UserError;
import com.example.sqlconnect.model.classes.response.UserResponse;
import com.example.sqlconnect.model.services.UserService;
import com.example.sqlconnect.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users") // Base path for all methods
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * GET /users/{userId} - Retrieve a specific user by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable String userId) {
        System.out.println("single user called");
        UserDto userEntity = userService.findByUserId(userId);

        if (userEntity == null) {
            // Return 404 Not Found if user does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Convert UserDto to UserResponse
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userEntity, userResponse);
        System.out.println(userResponse.toString());

        return ResponseEntity.ok(userResponse);
    }

    /**
     * GET /users - Retrieve all users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserDto> users = userService.getUsers();
        System.out.println("alluser called");
        // Convert List<UserDto> to List<UserResponse>
        List<UserResponse> responses = users.stream().map(userDto -> {
            UserResponse response = new UserResponse();
            BeanUtils.copyProperties(userDto, response);
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * POST /users - Create a new user
     */
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest)
            throws UserError {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);
        UserDto createdUser = userService.createUser(userDto);

        UserResponse returnValue = new UserResponse();
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    /**
     * PUT /users/{userId} - Update an existing user
     */
    @PutMapping()
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UserRequest userRequest)
            throws UserError {
        // check for null user and error handling
        UserDto userDto = userService.findByUserId(userRequest.getUserId());
        if (userDto == null) throw new UserError("user not found for update", "403");

        //update user on database
        BeanUtils.copyProperties(userRequest, userDto, "id");
        userDto.setEncryptedPassword(
                bCryptPasswordEncoder.encode(userRequest.getPassword()));
        UserDto updatedUser = userService.updateUser(userRequest.getUserId(),
                userDto);
        UserResponse returnValue = new UserResponse();
        BeanUtils.copyProperties(updatedUser, returnValue);

        return ResponseEntity.ok(returnValue);
    }
}
