package com.example.spaceRoulette.beast;

import com.example.spaceRoulette.planet.Planet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model of Beast Spreading")
@Entity
public class BeastSpreading {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value = 1, message = "Id must be bigger than 0")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "beast_id")
    private Beast beast;

    @ManyToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;

}
