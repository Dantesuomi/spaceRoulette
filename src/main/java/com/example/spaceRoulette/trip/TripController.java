package com.example.spaceRoulette.trip;


import com.example.spaceRoulette.trip.interfaces.TripService;
import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.UserController;
import com.example.spaceRoulette.user.UserProfileDto;
import com.example.spaceRoulette.user.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private static final Logger log = LoggerFactory.getLogger(TripController.class);

    @Autowired
    private TripService tripservice;

    @Autowired
    private UserService userService;

    @PostMapping("/performTrip")
    @Operation(summary = "Saves Trip by passing in valid username, email and password",
            description = "Saves trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "The request has succeeded"),
            @ApiResponse(responseCode = "400", description  = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description  = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description  = "Server error")})
    public ResponseEntity<Trip> performTrip(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody TripDto tripDto) {
        Long userId = user.getId();
        if (!userService.doesUserExistById(userId)) {
            log.warn("No users with id {}", userId);
            throw new IllegalArgumentException("No users with id " + userId);
        }


//        boolean isValidPassword = userService.isValidPassword(userDto.getPassword());
//        boolean isValidEmail = userService.isValidEmail(userDto.getEmail());
//        if (!isValidEmail) {
//            log.warn("Incorrect email input{}", userDto.getEmail());
//            throw new IllegalArgumentException("Incorrect email input " + userDto.getEmail());
//        } else if (!isValidPassword) {
//            log.warn("Password must include number, upper and lower case character and min length of 8");
//            throw new IllegalArgumentException("Password must include number, upper and lower case character and min length of 8");
//        }
//        try {
//            Trip savedTrip = .registerUser(userDto);
//            log.info("Trip saved successfully");
//            return ResponseEntity.ok(savedUser);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
        return null;
    }


}
