package com.example.spaceRoulette.trip.kafka;

import com.example.spaceRoulette.trip.Trip;
import com.example.spaceRoulette.trip.enums.TripResult;
import com.example.spaceRoulette.trip.interfaces.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@KafkaListener(topics = "trip-events", groupId = "trips", containerFactory = "listenerContainerFactory")
public class TripEventListener {
    private static final Logger log = LoggerFactory.getLogger(TripEventListener.class);

    @Autowired
    private TripRepository tripRepository;

    private final ConcurrentMap<Long, Trip> processedTrips = new ConcurrentHashMap<>();

    @KafkaHandler
    public void consumeTripEvent(TripEvent tripEvent) {
        try {
            log.info("Received Trip Event: " + tripEvent.getTrip() + ", Event Type - " + tripEvent.getEventType());

            if (tripEvent.getEventType().equals("created")) {
                Trip incomingTrip = tripEvent.getTrip();
                updateTripResult(incomingTrip);
                tripRepository.save(incomingTrip);
                //processedTrips.put(savedTrip.getId(), savedTrip);
            }
        }catch (Exception e){
            log.error("Error handling Trip Event: " + e.getMessage(), e);
        }
    }

    public Trip getProcessedTrip(Long tripId) {
        return processedTrips.get(tripId);
    }

    private void updateTripResult(Trip trip){
        if (!trip.getPlanet().getInhabitable()){
            trip.setTripResult(TripResult.UNSUCCESSFUL_TRIP);
            log.info("Trip with id {} was unsuccessful", trip.getId());
        }else {
            trip.setTripResult(TripResult.SUCCESSFUL_TRIP);
            log.info("Trip with id {} was successful", trip.getId());
        }
    }


}
