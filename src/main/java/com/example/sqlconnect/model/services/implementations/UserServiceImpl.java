package com.example.sqlconnect.model.services.implementations;

import com.example.sqlconnect.model.dbManager.UserEntity;
import com.example.sqlconnect.model.Repositories.UserRepository;
import com.example.sqlconnect.model.classes.response.UserError;
import com.example.sqlconnect.shared.dto.UserDto;
import com.example.sqlconnect.model.services.UserService;
import com.example.sqlconnect.utils.JwtUtil;
import com.example.sqlconnect.utils.PublicUserId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PublicUserId publicUserId;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtil jwtUtil = new JwtUtil();

    @Override
    public UserDto createUser(UserDto userDto) throws UserError {

        if(userRepository.findByEmail(userDto.getEmail()) != null) throw new UserError("User already exists", "401");

        UserEntity userEntity = new UserEntity();
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.setUserId(publicUserId.generateRandomId(30));
        System.out.println(userDto.toString());
        BeanUtils.copyProperties(userDto, userEntity);
        UserEntity createdUser = userRepository.save(userEntity);
        return userDto;
    }

    @Override
    public UserDto findByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return null; // Or throw a custom exception
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        return users.stream().map(userEntity -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) throws UserError {

        UserDto existingUser = findByUserId(userId);
        if(findByUserId(userId) == null) throw new UserError();
        //get current user for database
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        UserEntity recordedUser = userRepository.save(userEntity);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(recordedUser, returnedValue);

        return returnedValue;
    }

    @Override
    public String authenticateUser(String username, String password) {
        UserEntity user = userRepository.findByEmail(username);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getEncryptedPassword())) {
            // Assuming `getRoles` returns a comma-separated string like "ROLE_USER,ROLE_ADMIN"
            List<String> roles = List.of(user.getRoles().split(","));
            return jwtUtil.generateToken(username, roles);
        }
        return "userNotFound";
    }

    @Override
    public Boolean validateUser(String username, String password) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return null;
    }
}
