package com.example.spaceRoulette.user;

import com.example.spaceRoulette.planet.Planet;
import com.example.spaceRoulette.planet.interfaces.PlanetRepository;
import com.example.spaceRoulette.user.interfaces.UserRepository;
import com.example.spaceRoulette.user.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public static final String ROLE_USER = "ROLE_USER";


    @Override
    public User registerUser(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRole(ROLE_USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        log.info("Registering new User");
        userRepository.save(user);
        return user;
    }

    public boolean isValidPassword(String password) {
        // must include number, upper and lower case character and min length of 8
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        Pattern regex = Pattern.compile(pattern);
        return regex.matcher(password).matches();
    }

    public boolean isValidEmail(String email) {
        String pattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern regex = Pattern.compile(pattern);
        return regex.matcher(email).matches();
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = userRepository.findByUsername(username);

        return userDetail.map(User::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    @Override
    public User updateUser(Long id, UserProfileDto profileDto) {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        User updatedUser = userToUpdate.get();
        log.info("Updating User entry with ID {}", id);

        Long homePlanetId = profileDto.getPlanetId();

        Planet homePlanet = planetRepository.findById(homePlanetId)
                .orElseThrow(() -> new ResourceNotFoundException("Planet not found with ID: " + homePlanetId));

        updatedUser.setDateOfBirth(profileDto.getDateOfBirth());
        updatedUser.setHomePlanet(homePlanet);
        userRepository.save(updatedUser);

        log.info("Updated User entry with ID {}", id);
        return updatedUser;
    }

    @Override
    public Boolean doesUserExistById(Long userID) {
        return userRepository.existsById(userID);
    }
}
