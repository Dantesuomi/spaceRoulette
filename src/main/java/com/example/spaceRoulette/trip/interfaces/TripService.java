package com.example.spaceRoulette.trip.interfaces;

import com.example.spaceRoulette.ship.Ship;
import com.example.spaceRoulette.trip.Trip;
import com.example.spaceRoulette.trip.TripDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TripService {

    Long performTrip(TripDto tripDto);

    Optional<Trip> getTripById(Long tripId);

    List<Trip> getAllTripsByUserId (Long userId);

}
