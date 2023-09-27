package com.example.spaceRoulette.user.interfaces;

import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.UserDto;
import com.example.spaceRoulette.user.UserProfileDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean isValidPassword (String password);

    boolean isValidEmail (String email);
    User registerUser(UserDto userDto);
    User loadUserByUsername(String username);

    User updateUser(Long id, UserProfileDto profileDto);

    Boolean doesUserExistById(Long userID);
}
