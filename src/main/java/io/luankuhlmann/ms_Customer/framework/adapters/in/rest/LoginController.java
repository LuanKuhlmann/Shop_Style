package io.luankuhlmann.ms_Customer.framework.adapters.in.rest;

import io.luankuhlmann.ms_Customer.domain.dto.response.LoginResponseDTO;
import io.luankuhlmann.ms_Customer.domain.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.framework.adapters.out.rest.infra.security.TokenService;
import io.luankuhlmann.ms_Customer.application.service.LoginServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login Endpoint")
@RestController
@RequestMapping("/v1")
public class LoginController {
    @Autowired
    private LoginServiceImpl loginServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO body) {
        return loginServiceImpl.login(body);
    }
}
