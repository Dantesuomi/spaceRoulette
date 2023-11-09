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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ObjectMapper objectMapper;

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
            throw new IllegalArgumentException("Mismatch between required trip distance and ship travelling max distance, choose another ship or planet");
        }

        LocalDate dateOfTrip = tripDto.getDepartureDate();
        validateDepartureDate(dateOfTrip);
        trip.setDepartureDate(dateOfTrip);
        Trip savedTrip = tripRepository.save(trip);

        Long tripId = savedTrip.getId();

        kafkaTemplate.send("trip-events", new TripEvent(savedTrip, "created"));

        return tripId;
    }

    @Override
    @Cacheable(cacheNames = "trips", key = "#tripId")
    public String getTripByIdCached(Long tripId) {
        Optional<Trip> tripOptional = tripRepository.findById(tripId);
        if (tripOptional.isPresent()) {
            try {
                return objectMapper.writeValueAsString(tripOptional.get());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public Trip convertJsonToTrip(String json) {
        try {
            return objectMapper.readValue(json, Trip.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Trip> getAllTripsByUserId(Long userId){
        return tripRepository.findAllTripsByUserId(userId);
    }

    private boolean canShipTravelThisDistance(Ship chosenShip, Planet chosenPlanet){
        if(chosenShip.getMaxDistance()<chosenPlanet.getDistance()){
            log.info("Mismatch between required trip distance and ship max distance");
            return false;
        }
        return true;
    }

    private void validateDepartureDate(LocalDate departure_Date) {
        LocalDate currentDate = LocalDate.now();
        if (departure_Date.isBefore(currentDate)) {
            throw new IllegalArgumentException("Departure date cannot be in the past");
        }
    }
}
