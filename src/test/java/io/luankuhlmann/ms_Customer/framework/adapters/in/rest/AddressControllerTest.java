package io.luankuhlmann.ms_Customer.framework.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.luankuhlmann.ms_Customer.application.ports.in.AddressService;
import io.luankuhlmann.ms_Customer.domain.dto.request.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.domain.enums.Sex;
import io.luankuhlmann.ms_Customer.domain.enums.State;
import io.luankuhlmann.ms_Customer.domain.mapper.AddressMapper;
import io.luankuhlmann.ms_Customer.domain.models.Address;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AddressMapper addressMapper;

    private Address mockAddress;
    private AddressRequestDTO addressRequestDTO;

    @BeforeEach
    public void setup() {
        createAddressAndDtos();
    }

    @Test
    @WithMockUser
    public void testRegisterAddress() throws Exception {
        when(addressService.registerAddress(any(AddressRequestDTO.class))).thenReturn(ResponseEntity.status(201).build());

        mockMvc.perform(post("/v1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testUpdateAddress() throws Exception {
        when(addressService.updateAddress(anyLong(), any(AddressRequestDTO.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/v1/address/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteAddress() throws Exception {
        Mockito.doNothing().when(addressService).deleteAddress(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/address/1"))
                .andExpect(status().isNoContent());
    }


    private void createAddressAndDtos() {
        mockAddress = new Address();
        mockAddress.setId(1L);
        mockAddress.setState(State.SP);
        mockAddress.setCep("90000-000");
        mockAddress.setCity("Teste");
        mockAddress.setDistrict("Teste");
        mockAddress.setComplement("Teste");
        mockAddress.setNumber("101");
        mockAddress.setStreet("Teste");
        mockAddress.setCustomer(generateSampleCustomer());

        addressRequestDTO = addressMapper.mapToRequestDto(mockAddress);
    }

    private Customer generateSampleCustomer() {
        Customer mockCustomer = new Customer();
        mockCustomer.setFirstName("User");
        mockCustomer.setLastName("Admin");
        mockCustomer.setSex(Sex.Masculino);
        mockCustomer.setCpf("803.290.660-64");
        mockCustomer.setBirthdate(LocalDate.of(2000, 1, 1));
        mockCustomer.setEmail("user@email.com");
        mockCustomer.setPassword("12345678");
        mockCustomer.setActive(true);

        return mockCustomer;
    }
}