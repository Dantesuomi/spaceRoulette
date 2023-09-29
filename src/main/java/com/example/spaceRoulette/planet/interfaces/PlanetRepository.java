package com.example.spaceRoulette.planet.interfaces;

import com.example.spaceRoulette.planet.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {


}
