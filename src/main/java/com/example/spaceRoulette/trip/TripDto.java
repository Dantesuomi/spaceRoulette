package com.example.spaceRoulette.trip;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long shipId;

    @NotNull
    private Long planetId;

    @NotNull
    private LocalDate departureDate;


}
