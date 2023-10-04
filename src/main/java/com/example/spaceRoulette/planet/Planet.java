package com.example.spaceRoulette.planet;

import com.example.spaceRoulette.planet.enums.StarSystem;
import com.example.spaceRoulette.planet.enums.Atmosphere;
import com.example.spaceRoulette.planet.enums.Sector;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model of Planet")
@Entity
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value = 1, message = "Id must be bigger than 0")
    private Long id;

    private Double distance;

    private String name;

    private Sector sector;

    @Column(name = "star_system")
    private StarSystem starSystem;

    @Column(name = "mid_temperature")
    private Double midTemperature;

    private Atmosphere atmosphere;

    @Column(columnDefinition="TEXT")
    private String description;

    private Boolean inhabitable;
}
