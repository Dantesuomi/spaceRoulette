package com.example.spaceRoulette.enums;

import lombok.Getter;

@Getter
public enum TripResult {
    SUCCESSFUL_TRIP("Successful trip"), //"Congratulations, your trip was successful!"
    UNSUCCESSFUL_TRIP("Unsuccessful trip"); //"Your trip had some bad consequences, at least you discovered something new"


    private final String name;

    TripResult(String name) {
        this.name = name;
    }
}
