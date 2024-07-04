package io.luankuhlmann.ms_Customer.application.service;

import io.luankuhlmann.ms_Customer.application.ports.in.LoginService;
import io.luankuhlmann.ms_Customer.domain.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.domain.dto.response.LoginResponseDTO;
import io.luankuhlmann.ms_Customer.domain.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.domain.enums.Sex;
import io.luankuhlmann.ms_Customer.framework.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.framework.exceptions.InvalidPasswordException;
import io.luankuhlmann.ms_Customer.framework.adapters.out.rest.infra.security.TokenService;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import io.luankuhlmann.ms_Customer.framework.adapters.out.persistence.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO body) {
        Customer customer = customerRepository.findByEmail(body.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (passwordEncoder.matches(body.password(), customer.getPassword())) {
            String token = tokenService.generateToken(customer);
            return new LoginResponseDTO(customer.getEmail(), token);
        } else {
            throw new InvalidPasswordException("Invalid password");
        }
    }
}
