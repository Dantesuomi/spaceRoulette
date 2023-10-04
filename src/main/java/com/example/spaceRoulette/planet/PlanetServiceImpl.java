package com.example.spaceRoulette.planet;

import com.example.spaceRoulette.planet.interfaces.PlanetRepository;
import com.example.spaceRoulette.planet.interfaces.PlanetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanetServiceImpl implements PlanetService {

    private static final Logger log = LoggerFactory.getLogger(PlanetServiceImpl.class);
    @Autowired
    private PlanetRepository planetRepository;

    @Override
    public Optional<Planet> getPlanetById(Long planetId) {
        Optional<Planet> foundPlanet = planetRepository.findById(planetId);
        if (foundPlanet.isEmpty()) {
            log.info("Planet not found with id {}: ", planetId);
            throw new ResourceNotFoundException("Planet not found with id: " + planetId);
        }
        return foundPlanet;
    }

    @Override
    public Planet getPlanetInfoById(Long planetId) {
        Optional<Planet> planetForInfo = getPlanetById(planetId);
        if (planetForInfo.isEmpty()) {
                log.info("Planet not found with id {}: ", planetId);
                throw new ResourceNotFoundException("Planet not found with id: " + planetId);
        }
        return planetForInfo.get();

    }
}
