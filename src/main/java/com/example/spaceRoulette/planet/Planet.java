package com.example.spaceRoulette.planet;

import com.example.spaceRoulette.enums.StarSystem;
import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.enums.Atmosphere;
import com.example.spaceRoulette.enums.Sector;
import io.swagger.annotations.ApiModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model of Planet")
@Entity
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value = 1, message = "Id must be bigger than 0")
    private Long id;

    private Double distance;

    private String name;

    private Sector sector;

    private StarSystem starSystem;

    private Double midTemperature;

    private Atmosphere atmosphere;

    @Column(columnDefinition="TEXT")
    private String description;

    private Boolean inhabitable;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
