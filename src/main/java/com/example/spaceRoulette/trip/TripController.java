package com.example.spaceRoulette.trip;


import com.example.spaceRoulette.planet.Planet;
import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.ship.Ship;
import com.example.spaceRoulette.trip.interfaces.TripService;
import com.example.spaceRoulette.trip.kafka.TripEvent;
import com.example.spaceRoulette.trip.kafka.TripEventListener;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
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

    @Cacheable(cacheNames = "trips", key = "#tripId")
    public Trip getTripByIdCached(Long tripId) {
        return tripservice.getTripById(tripId).orElse(null);
    }

    @GetMapping("/{tripId}")
    @ApiOperation(value = "Get information about the trip by given id",
            notes = "Get trip information",
            response = Trip.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request has succeeded"),
            @ApiResponse(responseCode = "400", description = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<Trip> getTripInfo(
            @AuthenticationPrincipal User user,
            @PathVariable Long tripId
    ) {
        try{
            Trip trip = getTripByIdCached(tripId);
            if (trip == null) {
                throw new ResourceNotFoundException("Trip not found with id: " + tripId);
            }
            log.info("Getting info for trip with id " + tripId);
            return ResponseEntity.ok(trip);
        }catch (Exception e) {
            log.info("Unable to get trip info for given id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("trips/{userId}")
    @ApiOperation(value = "Get all trips information by given user id",
            notes = "Get all trips information",
            response = Trip.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request has succeeded"),
            @ApiResponse(responseCode = "400", description = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<List<Trip>> getAllTrips(@AuthenticationPrincipal User user,
                                  @PathVariable Long userId) {
        try {
            if (userId == null) {
                throw new ResourceNotFoundException("User ID cannot be null");
            }

            // Check if the logged-in user has the same userId as the requested userId
            if (!user.getId().equals(userId)) {
                throw new ResourceNotFoundException("You are not authorized to access this user's trips.");
            }

            List<Trip> trips = tripservice.getAllTripsByUserId(userId);
            if (trips.isEmpty()) {
                throw new ResourceNotFoundException("No trips found for user with ID: " + userId);
            }
            log.info("Getting trips info for user with id " + userId);
            return ResponseEntity.ok(trips);
        } catch (ResourceNotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unable to get trip info for the given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

