package com.example.spaceRoulette.tripTests;

import com.example.spaceRoulette.trip.Trip;
import com.example.spaceRoulette.trip.TripController;
import com.example.spaceRoulette.trip.interfaces.TripService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController tripController;

    Long tripId = 1L;



}
