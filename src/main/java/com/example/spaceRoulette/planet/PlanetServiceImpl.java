package com.example.spaceRoulette.planet;

import com.example.spaceRoulette.planet.interfaces.PlanetRepository;
import com.example.spaceRoulette.planet.interfaces.PlanetService;
import com.example.spaceRoulette.ship.Ship;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanetServiceImpl implements PlanetService {

    private PlanetRepository planetRepository;

    @Override
    public Optional<Planet> getPlanetById(Long planetId) {
        return planetRepository.findById(planetId);
    }
}
