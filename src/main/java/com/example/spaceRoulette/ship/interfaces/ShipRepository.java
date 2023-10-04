package com.example.spaceRoulette.ship.interfaces;

import com.example.spaceRoulette.ship.Ship;
import com.example.spaceRoulette.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
}
