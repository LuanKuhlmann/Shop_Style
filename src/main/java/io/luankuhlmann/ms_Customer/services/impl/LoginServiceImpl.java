package io.luankuhlmann.ms_Customer.services.impl;

import io.luankuhlmann.ms_Customer.dto.response.LoginResponseDTO;
import io.luankuhlmann.ms_Customer.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.exceptions.InvalidPasswordException;
import io.luankuhlmann.ms_Customer.infra.security.TokenService;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import io.luankuhlmann.ms_Customer.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO body) {
        Customer customer = findCustomerByEmail(body);

        if (passwordEncoder.matches(body.password(), customer.getPassword())) {
            String token = tokenService.generateToken(customer);
            LoginResponseDTO response = new LoginResponseDTO(customer.getEmail(), token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new InvalidPasswordException("Invalid password");
        }
    }

    private Customer findCustomerByEmail(LoginRequestDTO body) {
        return customerRepository.findByEmail(body.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
