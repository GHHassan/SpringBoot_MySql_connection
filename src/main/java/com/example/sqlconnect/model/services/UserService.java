package com.example.sqlconnect.model.services;

import com.example.sqlconnect.model.classes.response.UserError;
import com.example.sqlconnect.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto) throws UserError;
    List<UserDto> getUsers();  // Assuming this returns all users
    UserDto findByUserId(String userId);
    UserDto updateUser(String userId, UserDto userDto) throws UserError;

    String authenticateUser(String username, String password);
    Boolean validateUser(String username, String password);
}

