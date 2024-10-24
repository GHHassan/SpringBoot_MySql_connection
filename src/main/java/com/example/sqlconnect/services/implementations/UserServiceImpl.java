package com.example.sqlconnect.services.implementations;

import com.example.sqlconnect.io.entity.UserRecord;
import com.example.sqlconnect.model.Repositories.UserRepository;
import com.example.sqlconnect.model.response.UserError;
import com.example.sqlconnect.shared.dto.UserDto;
import com.example.sqlconnect.services.UserService;
import com.example.sqlconnect.shared.utils.PublicUserId;
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

    @Override
    public UserDto createUser(UserDto userDto) throws UserError {

        if(userRepository.findByEmail(userDto.getEmail()) != null) throw new UserError("User already exists", "401");

        UserRecord userRecord = new UserRecord();
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.setUserId(publicUserId.generateRandomId(30));
        System.out.println(userDto.toString());
        BeanUtils.copyProperties(userDto, userRecord);
        UserRecord createdUser = userRepository.save(userRecord);
        return userDto;
    }

    @Override
    public UserDto findByUserId(String userId) {
        UserRecord userRecord = userRepository.findByUserId(userId);
        if (userRecord == null) {
            return null; // Or throw a custom exception
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRecord, userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserRecord> users = (List<UserRecord>) userRepository.findAll();
        return users.stream().map(userRecord -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userRecord, userDto);
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) throws UserError {

        if(findByUserId(userId) == null) throw new UserError();
        //get current user for database
        UserRecord userRecord = new UserRecord();
        BeanUtils.copyProperties(userDto, userRecord);

        UserRecord recordedUser = userRepository.save(userRecord);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(recordedUser, returnedValue);

        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
