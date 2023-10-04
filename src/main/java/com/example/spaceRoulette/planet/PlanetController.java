package com.example.spaceRoulette.planet;

import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.trip.TripController;
import com.example.spaceRoulette.user.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planet")
public class PlanetController {

    private static final Logger log = LoggerFactory.getLogger(TripController.class);

    @Autowired
    private PlanetService planetService;

    @GetMapping("/getPlanetInfo/{planetId}")
    @ApiOperation(value = "Get information about the planet after the trip",
            notes = "Get planet information",
            response = Planet.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request has succeeded"),
            @ApiResponse(responseCode = "400", description = "The server has a Bad Request and cannot process the invalid request"),
            @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URL"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<Planet> getPlanetInfo(
            @AuthenticationPrincipal User user,
            @PathVariable Long planetId
    ) {
        try{
            Planet chosenPlanet = planetService.getPlanetInfoById(planetId);
            log.info("Getting info for chosen planet");
            return ResponseEntity.ok(chosenPlanet);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
