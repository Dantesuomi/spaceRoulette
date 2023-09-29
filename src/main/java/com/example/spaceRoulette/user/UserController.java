package com.example.spaceRoulette.user;

import com.example.spaceRoulette.user.interfaces.UserService;
import com.example.spaceRoulette.user.jwt.AuthRequest;
import com.example.spaceRoulette.user.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String helloWorld(@AuthenticationPrincipal User user) {
        return "Hello world " + user.getUsername();
    }

    @PostMapping("/register")
    @Operation(summary = "Saves user by passing in valid username, email and password",
            description = "Saves user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "The request has succeeded"),
            @ApiResponse(responseCode = "400", description  = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description  = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description  = "Server error")})
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

    @GetMapping("/userProfile")
    //@PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @PutMapping
    @ApiOperation(value = "Update logged in user",
            notes = "Update logged in user" ,
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "The request has succeeded"),
            @ApiResponse(responseCode = "400", description  = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description  = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description  = "Server error")})
    public ResponseEntity<User> updateUserUsingId
            (@AuthenticationPrincipal User user,
             @Valid @RequestBody UserProfileDto userProfileDto) {
        Long userId = user.getId();
        if (!userService.doesUserExistById(userId)) {
            log.warn("No users with id {}", userId);
            throw new IllegalArgumentException("No users with id " + userId);
        }
        User updatedUser = userService.updateUser(userId, userProfileDto);
        log.info("User with id {} is updated.", userId);
        return ResponseEntity.ok(updatedUser);
    }
}
