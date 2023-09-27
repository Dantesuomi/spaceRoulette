package com.example.spaceRoulette.user;

import com.example.spaceRoulette.planet.Planet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model of User")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value = 1, message = "Id must be bigger than 0")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String username;

    private LocalDate dateOfBirth;

    //private String nameOfProfilePhoto;
    @ManyToOne
    @JoinColumn(name = "homePlanet_id")
    private Planet homePlanet;

    private String role;

    @CreationTimestamp
    private Date createdAt;

    private List<GrantedAuthority> authorities;


    public User(User user) {
        id = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        username = user.getUsername();
        dateOfBirth = user.getDateOfBirth();
        homePlanet = user.getHomePlanet();
        createdAt = user.getCreatedAt();
        role = user.getRole();
        authorities = Arrays.stream(user.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
