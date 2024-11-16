package com.example.sqlconnect.controller;


import com.example.sqlconnect.model.classes.requests.UserRequest;
import com.example.sqlconnect.model.classes.response.UserError;
import com.example.sqlconnect.model.classes.response.UserResponse;
import com.example.sqlconnect.model.services.UserService;
import com.example.sqlconnect.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("signup")
public class SignupController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * POST /users - Create a new user
     */
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest)
            throws UserError {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);
        if(userDto.getRoles() == null) userDto.setRoles("ROLES_USER");
        UserDto createdUser = userService.createUser(userDto);
        UserResponse returnValue = new UserResponse();
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }
}
