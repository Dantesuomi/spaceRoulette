package com.example.spaceRoulette.ship;


import com.example.spaceRoulette.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ShipService {

    Optional<Ship> getShipById(Long shipId);

}
