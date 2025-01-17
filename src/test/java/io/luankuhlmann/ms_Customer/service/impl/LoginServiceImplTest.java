package io.luankuhlmann.ms_Customer.service.impl;

import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.services.impl.LoginServiceImpl;
import io.luankuhlmann.ms_Customer.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.LoginResponseDTO;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import io.luankuhlmann.ms_Customer.infra.security.TokenService;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceImplTest {
    @InjectMocks
    LoginServiceImpl loginService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    TokenService tokenService;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully log a user and return a auth token")
    public void testLoginSuccess() {
        String email = "test@example.com";
        String password = "password123";
        Customer mockCustomer = new Customer();
        mockCustomer.setEmail(email);
        mockCustomer.setPassword(passwordEncoder.encode(password));

        Mockito.when(customerRepository.findByEmail(email)).thenReturn(Optional.of(mockCustomer));

        Mockito.when(passwordEncoder.matches(password, mockCustomer.getPassword())).thenReturn(true);

        String mockToken = "mocked.token.string";
        Mockito.when(tokenService.generateToken(mockCustomer)).thenReturn(mockToken);

        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);
        ResponseEntity<LoginResponseDTO> response = loginService.login(loginRequest);

        assertNotNull(response);
        assertEquals(email, Objects.requireNonNull(response.getBody()).email());
        assertEquals(mockToken, response.getBody().token());

        Mockito.verify(customerRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(password, mockCustomer.getPassword());
        Mockito.verify(tokenService, Mockito.times(1)).generateToken(mockCustomer);
    }

    @Test
    @DisplayName("Should return a exception when the user try to log with a incorrect password")
    public void testInvalidPasswordLogin() {
        String email = "test@example.com";
        String password = "wrongpassword";
        Customer mockCustomer = new Customer();
        mockCustomer.setEmail(email);
        mockCustomer.setPassword(passwordEncoder.encode("correctpassword"));

        Mockito.when(customerRepository.findByEmail(email)).thenReturn(Optional.of(mockCustomer));

        Mockito.when(passwordEncoder.matches(password, mockCustomer.getPassword())).thenReturn(false);

        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);

        assertThrows(InvalidPasswordException.class, () -> {
            loginService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("Should return a exception when the user inform a not registered email")
    public void invalidEmailLogin() {
        String email = "nonexistent@example.com";

        Mockito.when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        LoginRequestDTO loginRequest = new LoginRequestDTO(email, "anypassword");

        assertThrows(EntityNotFoundException.class, () -> {
            loginService.login(loginRequest);
        });
    }
}