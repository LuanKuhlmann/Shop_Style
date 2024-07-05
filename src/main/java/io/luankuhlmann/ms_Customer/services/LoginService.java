package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.LoginResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    LoginResponseDTO login(LoginRequestDTO body);
}