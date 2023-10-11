package com.example.spaceRoulette.trip;


import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.trip.interfaces.TripService;
import com.example.spaceRoulette.trip.kafka.TripEvent;
import com.example.spaceRoulette.trip.kafka.TripEventListener;
import com.example.spaceRoulette.user.User;
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

    @Autowired
    private PlanetService planetService;

    @Autowired
    private TripEventListener tripEventListener;

    private TripEvent tripEvent;

    @PostMapping("/performTrip")
    @Operation(summary = "Saves Trip by passing in valid username, email, and password",
            description = "Saves trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The trip was saved successfully"),
            @ApiResponse(responseCode = "400", description = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<TripStatus> performTrip(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody TripDto tripDto) {
        try {
            Long savedTripId = tripservice.performTrip(tripDto);

            if (savedTripId != null) {
                log.info("Trip saved successfully with ID: " + savedTripId);
                TripStatus tripStatus = new TripStatus("created", savedTripId);
                return ResponseEntity.status(HttpStatus.CREATED).body(tripStatus);
            } else {
                log.error("Failed to retrieve the saved trip from Kafka");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            log.error("Failed to perform the trip", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}

