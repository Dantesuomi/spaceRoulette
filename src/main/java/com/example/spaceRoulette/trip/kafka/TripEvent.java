package com.example.spaceRoulette.trip.kafka;

import com.example.spaceRoulette.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripEvent {

    private Trip trip;
    private String eventType;
}
