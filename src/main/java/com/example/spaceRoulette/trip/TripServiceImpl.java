package com.example.spaceRoulette.trip;

import com.example.spaceRoulette.trip.enums.TripResult;
import com.example.spaceRoulette.planet.Planet;
import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.ship.interfaces.ShipService;
import com.example.spaceRoulette.trip.interfaces.TripRepository;
import com.example.spaceRoulette.trip.interfaces.TripService;
import com.example.spaceRoulette.ship.Ship;
import com.example.spaceRoulette.trip.kafka.TripEvent;
import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.interfaces.UserRepository;
import com.example.spaceRoulette.user.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService {

    private static final Logger log = LoggerFactory.getLogger(TripService.class);

    @Autowired
    private ShipService shipService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private TripRepository tripRepository;

    private TripResult tripResult;

    @Autowired
    private KafkaTemplate<String, TripEvent> kafkaTemplate;



    public Long performTrip(TripDto tripDto){

        Trip trip = new Trip();

        Optional<User> user = userRepository.findById(tripDto.getUserId());
        if (user.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + tripDto.getUserId());
        }
        User travellingUser = user.get();
        trip.setUser(travellingUser);

        Optional<Ship> ship = shipService.getShipById(tripDto.getShipId());
        if (ship.isEmpty()) {
            throw new ResourceNotFoundException("Ship not found with id: " + tripDto.getShipId());
        }
        Ship chosenShip = ship.get();
        trip.setShip(chosenShip);

        Optional<Planet> planet = planetService.getPlanetById(tripDto.getPlanetId());
        if (planet.isEmpty()) {
            throw new ResourceNotFoundException("Planet not found with id " + tripDto.getPlanetId());
        }
        Planet chosenPlanet = planet.get();
        trip.setPlanet(chosenPlanet);

        if (!canShipTravelThisDistance(chosenShip, chosenPlanet)){
            throw new IllegalArgumentException("Mismatch between required trip distance and ship max distance, choose another ship or planet");
        }

        LocalDate dateOfTrip = tripDto.getDepartureDate();
        validateDepartureDate(dateOfTrip);
        trip.setDepartureDate(dateOfTrip);

        //TODO
        trip.setId(UUID.randomUUID().getMostSignificantBits());
        kafkaTemplate.send("trip-events", new TripEvent(trip, "created"));

        return trip.getId();
    }

    private boolean canShipTravelThisDistance(Ship chosenShip, Planet chosenPlanet){
        if(chosenShip.getMaxDistance()<chosenPlanet.getDistance()){
            log.info("Mismatch between required trip distance and ship max distance");
            return false;
        }
        return true;
    }

    public void validateDepartureDate(LocalDate departure_Date) {
        LocalDate currentDate = LocalDate.now();
        if (departure_Date.isBefore(currentDate)) {
            throw new IllegalArgumentException("Departure date cannot be in the past");
        }
    }

}
