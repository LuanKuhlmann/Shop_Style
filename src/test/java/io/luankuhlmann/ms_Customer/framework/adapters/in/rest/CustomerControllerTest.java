package io.luankuhlmann.ms_Customer.framework.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.luankuhlmann.ms_Customer.application.ports.in.CustomerService;
import io.luankuhlmann.ms_Customer.domain.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.domain.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.domain.enums.Sex;
import io.luankuhlmann.ms_Customer.domain.enums.State;
import io.luankuhlmann.ms_Customer.domain.mapper.CustomerMapper;
import io.luankuhlmann.ms_Customer.domain.models.Address;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CustomerMapper customerMapper;

    private CustomerResponseDTO customerResponseDTO;
    private CustomerRequestDTO customerRequestDTO;
    private Customer mockCustomer;

    @BeforeEach
    public void setup() {
        createCustomerAndDtos();
    }

    @Test
    @WithMockUser
    public void testGetCustomer() throws Exception {
        when(customerService.getCustomer(anyLong())).thenReturn(customerResponseDTO);

        mockMvc.perform(get("/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponseDTO)));
    }

    @Test
    public void testRegisterCustomer() throws Exception {
        when(customerService.registerCustomer(any(CustomerRequestDTO.class))).thenReturn(ResponseEntity.status(201).build());

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(anyLong(), any(CustomerRequestDTO.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testUpdatePassword() throws Exception {
        when(customerService.updatePassword(anyLong(), any(String.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/v1/customers/1/")
                        .param("password", "newpassword123"))
                .andExpect(status().isOk());
    }

    private void createCustomerAndDtos() {
        mockCustomer = new Customer();
        mockCustomer.setFirstName("User");
        mockCustomer.setLastName("Admin");
        mockCustomer.setSex(Sex.Masculino);
        mockCustomer.setCpf("803.290.660-64");
        mockCustomer.setBirthdate(LocalDate.of(2000, 1, 1));
        mockCustomer.setEmail("user@email.com");
        mockCustomer.setPassword("12345678");
        mockCustomer.setActive(true);
        mockCustomer.setAddresses(generateSampleAddresses());

        customerResponseDTO = customerMapper.mapToResponseDTO(mockCustomer);

        customerRequestDTO = customerMapper.mapToRequestDTO(mockCustomer);
    }

    private List<Address> generateSampleAddresses() {
        List<Address> mockAddresses = new ArrayList<>();

        Address mockAddress = new Address();
        mockAddress.setState(State.SP);
        mockAddress.setCity("Teste");
        mockAddress.setDistrict("Teste");
        mockAddress.setStreet("Teste");
        mockAddress.setNumber("101");
        mockAddress.setCep("90000-000");
        mockAddress.setComplement("Teste 101");
        mockAddress.setCustomer(mockCustomer);

        mockAddresses.add(mockAddress);

        return mockAddresses;
    }
}