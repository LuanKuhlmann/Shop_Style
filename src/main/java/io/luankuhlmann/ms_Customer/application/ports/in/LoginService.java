package io.luankuhlmann.ms_Customer.application.ports.in;

import io.luankuhlmann.ms_Customer.domain.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.domain.dto.response.LoginResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    public LoginResponseDTO login(LoginRequestDTO body);
}
