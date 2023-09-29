package com.example.spaceRoulette.trip;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private Long userId;

    @NotNull
    @NotEmpty
    private Long shipId;

    @NotNull
    @NotEmpty
    private Long planetId;

    @NotNull
    @NotEmpty
    private LocalDate departure_Date;


}
