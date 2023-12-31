package com.example.spaceRoulette.ship;

import com.example.spaceRoulette.ship.enums.ShipType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model of Ship")
@Entity
    public class Ship {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Min(value = 1, message = "Id must be bigger than 0")
        private Long id;

        private ShipType shipType;

        private String name;

        private Double maxDistance;

        @Column(columnDefinition="TEXT")
        private String description;
    }
