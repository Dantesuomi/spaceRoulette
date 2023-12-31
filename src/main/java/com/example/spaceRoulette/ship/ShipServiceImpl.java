package com.example.spaceRoulette.ship;


import com.example.spaceRoulette.ship.interfaces.ShipRepository;
import com.example.spaceRoulette.ship.interfaces.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Optional<Ship> getShipById(Long shipId) {
        return shipRepository.findById(shipId);
    }
}