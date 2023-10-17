package com.example.spaceRoulette.userTests;

import com.example.spaceRoulette.planet.interfaces.PlanetRepository;
import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.UserDto;
import com.example.spaceRoulette.user.UserProfileDto;
import com.example.spaceRoulette.user.UserServiceImpl;
import com.example.spaceRoulette.user.interfaces.UserRepository;
import com.example.spaceRoulette.planet.Planet;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    UserDto userDto = new UserDto("Pikachu", "pikachu@example.com", "1234!!!!BALLpikapika");

    UserProfileDto profileDto = new UserProfileDto(LocalDate.of(2000, 1, 1), 1L);

    private final Long userId = 1L;

    private final Long planetId = 1L;
    private final LocalDate futureDateOfBirth = LocalDate.now().plusYears(1);
    @Test
    public void testRegisterUser() {

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashedPassword");

        User registeredUser = userService.registerUser(userDto);

        assertEquals("Pikachu", registeredUser.getUsername());
        assertEquals("pikachu@example.com", registeredUser.getEmail());
        assertEquals(UserServiceImpl.ROLE_USER, registeredUser.getRole());
        assertEquals("hashedPassword", registeredUser.getPassword());

        verify(userRepository).save(registeredUser);
    }

    @Test
    public void testRegisterUser_WithExistingUsername() {

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userDto);
        });

        assertEquals("Username is already taken", exception.getMessage());
    }

    @Test
    public void testRegisterUser_WithExistingEmail() {

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userDto);
        });

        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        Long homePlanetId = profileDto.getPlanetId();
        Planet homePlanet = new Planet();
        homePlanet.setId(homePlanetId);
        when(planetRepository.findById(homePlanetId)).thenReturn(Optional.of(homePlanet));

        User updatedUser = userService.updateUser(userId, profileDto);

        assertNotNull(updatedUser);
        assertEquals(profileDto.getDateOfBirth(), updatedUser.getDateOfBirth());
        assertEquals(homePlanet, updatedUser.getHomePlanet());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, profileDto));
    }

    @Test
    public void testUpdateUser_WithFutureDateOfBirth() {

        profileDto.setDateOfBirth(futureDateOfBirth);
        profileDto.setPlanetId(planetId);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        Long homePlanetId = profileDto.getPlanetId();
        Planet homePlanet = new Planet();
        homePlanet.setId(homePlanetId);
        when(planetRepository.findById(homePlanetId)).thenReturn(Optional.of(homePlanet));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, profileDto));

    }
}

    


