package com.example.spaceRoulette.planet.interfaces;

import com.example.spaceRoulette.planet.Planet;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PlanetService {

    Optional <Planet> getPlanetById(Long planetId);
    public Planet getPlanetInfoById(Long planetId);
}
