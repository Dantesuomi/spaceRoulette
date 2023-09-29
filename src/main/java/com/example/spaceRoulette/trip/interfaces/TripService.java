package com.example.spaceRoulette.trip.interfaces;

import com.example.spaceRoulette.trip.Trip;
import com.example.spaceRoulette.trip.TripDto;
import com.example.spaceRoulette.user.User;
import org.springframework.stereotype.Service;

@Service
public interface TripService {

    Trip performTrip(TripDto tripDto);
}
