package com.example.spaceRoulette.configuration;

import com.example.spaceRoulette.user.jwt.JwtAuthFilter;
import com.example.spaceRoulette.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("api/user/login").permitAll()
                                .requestMatchers("api/user/register").anonymous()
                                .requestMatchers(HttpMethod.PUT, "/api/user").hasRole("USER")
                                .requestMatchers("api/user/userProfile").hasRole("USER") //works fine
                                .requestMatchers("api/trip/performTrip").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/api/planet/getPlanetInfo/{planetId}").hasRole("USER")
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .sessionConcurrency((sessionConcurrency) ->
                                        sessionConcurrency
                                                .maximumSessions(1)
                                                .expiredUrl("/login?expired")
                                )

        );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 }