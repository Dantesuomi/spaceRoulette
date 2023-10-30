package com.example.spaceRoulette.tripTests;

import com.example.spaceRoulette.trip.Trip;
import com.example.spaceRoulette.trip.TripController;
import com.example.spaceRoulette.trip.TripServiceImpl;
import com.example.spaceRoulette.trip.interfaces.TripService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TripControllerTest {

    @Mock
    private MockMvc mockMvc;
    @Mock
    private TripServiceImpl tripService;

    @InjectMocks
    private TripController tripController;


    Long tripId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTripInfoSuccess() {

        Trip mockTrip = new Trip();
        Mockito.when(tripService.getTripById(tripId)).thenReturn(Optional.of(mockTrip));

        ResponseEntity<Trip> response = tripController.getTripInfo(null, tripId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTrip_TripNotFound() {
        Mockito.when(tripService.getTripById(tripId)).thenReturn(Optional.empty());

        ResponseEntity<Trip> response = tripController.getTripInfo(null, tripId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }




}
