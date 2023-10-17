package com.example.spaceRoulette.userTests;

import com.example.spaceRoulette.user.User;
import com.example.spaceRoulette.user.UserController;
import com.example.spaceRoulette.user.UserDto;
import com.example.spaceRoulette.user.UserProfileDto;
import com.example.spaceRoulette.user.interfaces.UserService;
import com.example.spaceRoulette.user.jwt.AuthRequest;
import com.example.spaceRoulette.user.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final AuthRequest authRequest = new AuthRequest("Pikachu", "1234!!!!BALLpikapika");

    UserDto userDto = new UserDto("Pikachu", "pikachu@example.com", "1234!!!!BALLpikapika");

    UserProfileDto profileDto = new UserProfileDto(LocalDate.of(2000, 1, 1), 1L);

    private final Long userId = 1L;



    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void testAuthenticateAndGetToken_Success() throws Exception {

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);

        String username = "Pikachu";
        String token = "mocked-jwt-token";
        when(jwtService.generateToken(username)).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(token));
    }

    @Test
    public void testAuthenticateAndGetToken_InvalidUser() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("Invalid user request !"));

        try {
            String token = userController.authenticateAndGetToken(authRequest);
        } catch (UsernameNotFoundException e) {
            assert (e.getMessage()).equals("Invalid user request !");
        }
    }

    private String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void testRegisterUser_Success() {

        when(userService.isValidEmail(userDto.getEmail())).thenReturn(true);
        when(userService.isValidPassword(userDto.getPassword())).thenReturn(true);

        User savedUser = new User();
        when(userService.registerUser(userDto)).thenReturn(savedUser);

        ResponseEntity<User> responseEntity = userController.registerUser(userDto);

        int actualStatusCode = responseEntity.getStatusCode().value();
        int expectedStatusCode = HttpStatus.CREATED.value();

        assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
        assertThat(responseEntity.getBody()).isEqualTo(savedUser);
    }

    @Test
    public void testRegisterUser_InvalidEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail("invalidPikachu@pika.com");
        when(userService.isValidEmail(userDto.getEmail())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.registerUser(userDto);
        });

        assertEquals("Incorrect email input " + userDto.getEmail(), exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidPassword() {

        UserDto userDto = new UserDto();
        userDto.setPassword("weakpass");

        when(userService.isValidEmail(userDto.getEmail())).thenReturn(true);
        when(userService.isValidPassword(userDto.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.registerUser(userDto);
        });
        assertEquals("Password must include number, upper and lower case character and min length of 8", exception.getMessage());
    }

    @Test
    @WithMockUser
    public void testUpdateUser() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("pepega@pepega.lv");

        when(userService.doesUserExistById(1L)).thenReturn(true);
        when(userService.updateUser(anyLong(), any(UserProfileDto.class))).thenReturn(mockUser);

        ResponseEntity<User> response = userController.updateUserUsingId(mockUser, profileDto);

        verify(userService).doesUserExistById(1L);
        verify(userService).updateUser(1L, profileDto);

        int actualStatusCode = response.getStatusCode().value();
        int expectedStatusCode = HttpStatus.OK.value();

        assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
        assertEquals(mockUser, response.getBody());

    }



}
