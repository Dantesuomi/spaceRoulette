package com.example.spaceRoulette.tripTests;

import com.example.spaceRoulette.planet.Planet;
import com.example.spaceRoulette.planet.enums.Atmosphere;
import com.example.spaceRoulette.planet.enums.Sector;
import com.example.spaceRoulette.planet.enums.StarSystem;
import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.ship.Ship;
import com.example.spaceRoulette.ship.enums.ShipType;
import com.example.spaceRoulette.ship.interfaces.ShipService;
import com.example.spaceRoulette.trip.Trip;
import com.example.spaceRoulette.trip.TripDto;
import com.example.spaceRoulette.trip.TripServiceImpl;
import com.example.spaceRoulette.trip.interfaces.TripRepository;
import com.example.spaceRoulette.trip.interfaces.TripService;
import com.example.spaceRoulette.trip.kafka.TripEvent;
import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)

    public class TripServiceTest {

    @InjectMocks
    private TripServiceImpl tripService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShipService shipService;
    @Mock
    private PlanetService planetService;
    @Mock
    private KafkaTemplate<String, TripEvent> kafkaTemplate;


    TripDto tripDto = new TripDto(1L, 2L, 2L, LocalDate.of(2030, 1, 1));
    TripDto invalidDateTripDto = new TripDto(1L, 2L, 2L, LocalDate.now().minusDays(1));

    Ship ship = new Ship(1L, ShipType.CARGO_SHIP, "Cargo Ship 1", 1000.0, "Very big ship");

    Planet planet = new Planet(1L, 42.0, "Earth", Sector.NEXUS, StarSystem.AURORA_SKYE, 15.0, Atmosphere.ICE_GIANT, "The third planet from the Sun.", true);


    @Test
    public void testPerformTrip() {

        User user = new User();
        //when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Ship ship = new Ship();
        Planet planet = new Planet();
        Long tripId = tripService.performTrip(tripDto);

        assertNotNull(tripId);
        //verify(kafkaTemplate, times(1)).send(eq("trip-events"), any(TripEvent.class));
    }

    @Test
    public void testPerformTrip_UserNotFound(){

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            tripService.performTrip(tripDto);
        });

        String expectedErrorMessage = "User not found with id: " + tripDto.getUserId();
        String actualErrorMessage = exception.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);

        verify(shipService, never()).getShipById(anyLong());
        verify(planetService, never()).getPlanetById(anyLong());

    }

    @Test
    public void testPerformTrip_PlanetNotFound() {

        when(userRepository.findById(tripDto.getUserId())).thenReturn(Optional.of(new User()));
        when(shipService.getShipById(tripDto.getShipId())).thenReturn(Optional.of(new Ship()));
        when(planetService.getPlanetById(tripDto.getPlanetId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            tripService.performTrip(tripDto);
        });

        String expectedMessage = "Planet not found with id " + tripDto.getPlanetId();
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testPerformTrip_ShipNotFound() {

        when(userRepository.findById(tripDto.getUserId())).thenReturn(Optional.of(new User()));
        when(shipService.getShipById(tripDto.getShipId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            tripService.performTrip(tripDto);
        });

        String expectedMessage = "Ship not found with id: " + tripDto.getShipId();
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testPerformTrip_DepartureDateIsInPast() {

        when(userRepository.findById(invalidDateTripDto.getUserId())).thenReturn(Optional.of(new User()));
        when(shipService.getShipById(invalidDateTripDto.getShipId())).thenReturn(Optional.of(ship));
        when(planetService.getPlanetById(invalidDateTripDto.getPlanetId())).thenReturn(Optional.of(planet));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tripService.performTrip(invalidDateTripDto);
        });

        String expectedMessage = "Departure date cannot be in the past";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}
