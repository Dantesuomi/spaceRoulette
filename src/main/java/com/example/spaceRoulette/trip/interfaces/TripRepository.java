package com.example.spaceRoulette.trip.interfaces;

import com.example.spaceRoulette.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllTripsByUserId(Long userId);
}
