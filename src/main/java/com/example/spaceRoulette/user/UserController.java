package com.example.spaceRoulette.user;

import com.example.spaceRoulette.user.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "Saves user by passing in valid username, email and password",
            notes = "Saves user" ,
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message ="The request has succeeded"),
            @ApiResponse(code = 400, message = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(code = 404, message =  "The server has not found anything matching the Request-URL"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserDto userDto) {
        boolean isValidPassword = userService.isValidPassword(userDto.getPassword());
        boolean isValidEmail = userService.isValidEmail(userDto.getEmail());
        if (!isValidEmail) {
            log.warn("Incorrect email input{}", userDto.getEmail());
            throw new IllegalArgumentException("Incorrect email input " + userDto.getEmail());
        } else if (!isValidPassword) {
            log.warn("Password must include number, upper and lower case character and min length of 8");
            throw new IllegalArgumentException("Password must include number, upper and lower case character and min length of 8");
        }
        try {
            User savedUser = userService.registerUser(userDto);
            log.info("User registered successfully");
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
