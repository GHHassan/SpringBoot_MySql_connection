package com.example.sqlconnect.controller;

import com.example.sqlconnect.model.classes.requests.LoginRequest;
import com.example.sqlconnect.model.classes.response.LoginResponse;
import com.example.sqlconnect.model.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login") // Base path for all methods
public class AuthController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    /*
        accepts and validates login request which includes username and password
        matches against database using userService interface.

        more security measures can be added to this method such as tracking
        the number of times a user tried with wrong credentials
        or adding 2FA
     */
    @PostMapping
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user and get the JWT token
            String jwt = userService.authenticateUser(
                    loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}
