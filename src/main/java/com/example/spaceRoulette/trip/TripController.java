package com.example.spaceRoulette.trip;


import com.example.spaceRoulette.planet.Planet;
import com.example.spaceRoulette.planet.PlanetServiceImpl;
import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.trip.interfaces.TripService;
import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/trip")
public class TripController {

    private static final Logger log = LoggerFactory.getLogger(TripController.class);

    @Autowired
    private TripService tripservice;

    @Autowired
    private UserService userService;

    @Autowired
    private PlanetService planetService;

    @PostMapping("/performTrip")
    @Operation(summary = "Saves Trip by passing in valid username, email and password",
            description = "Saves trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The trip was saved successfully"),
            @ApiResponse(responseCode = "400", description = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<Trip> performTrip(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody TripDto tripDto) {
        try {
            Trip savedTrip = tripservice.performTrip(tripDto);
            log.info("Trip saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTrip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }


}