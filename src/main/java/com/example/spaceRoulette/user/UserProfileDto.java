package com.example.spaceRoulette.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {

    private LocalDate dateOfBirth;
    private Long planetId;

}
