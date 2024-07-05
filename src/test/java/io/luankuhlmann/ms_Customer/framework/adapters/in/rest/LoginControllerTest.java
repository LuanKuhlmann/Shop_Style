package io.luankuhlmann.ms_Customer.framework.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.luankuhlmann.ms_Customer.services.LoginServiceImpl;
import io.luankuhlmann.ms_Customer.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.LoginResponseDTO;
import io.luankuhlmann.ms_Customer.infra.security.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TokenService tokenService;

    @Test
    @WithMockUser
    public void testLogin() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("test@email.com", "token");

        Mockito.when(loginService.login(Mockito.any(LoginRequestDTO.class)))
                .thenReturn(loginResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));
    }
}
