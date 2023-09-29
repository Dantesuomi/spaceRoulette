package com.example.spaceRoulette.ship;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Override
    public Optional<Ship> getShipById(Long shipId) {
        return shipRepository.findById(shipId);
    }
}