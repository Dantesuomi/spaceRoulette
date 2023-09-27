package com.example.spaceRoulette.planet.interfaces;

import com.example.spaceRoulette.planet.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {


}
