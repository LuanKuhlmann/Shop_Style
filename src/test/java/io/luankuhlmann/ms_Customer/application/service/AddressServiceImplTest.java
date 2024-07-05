package io.luankuhlmann.ms_Customer.application.service;

import io.luankuhlmann.ms_Customer.services.AddressServiceImpl;
import io.luankuhlmann.ms_Customer.dto.request.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.models.enums.State;
import io.luankuhlmann.ms_Customer.mapper.AddressMapper;
import io.luankuhlmann.ms_Customer.models.Address;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.AddressRepository;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {
    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressMapper addressMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully register a new address when everything is ok")
    public void testRegisterAddress() {
        Customer customer = new Customer();
        customer.setId(1L);

        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();
        Address newAddress = new Address();

        when(customerRepository.findById(addressRequestDTO.customerId())).thenReturn(Optional.of(customer));
        when(addressMapper.mapToEntity(addressRequestDTO)).thenReturn(newAddress);

        ResponseEntity response = addressService.registerAddress(addressRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(customerRepository, times(1)).findById(customer.getId());
        verify(addressMapper, times(1)).mapToEntity(addressRequestDTO);
        verify(addressRepository, times(1)).save(newAddress);
    }

    @Test
    @DisplayName("Should throw a exception when customer id was not found")
    public void testRegisterAddressCustomerNotFound() {
        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();

        when(customerRepository.findById(addressRequestDTO.customerId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            addressService.registerAddress(addressRequestDTO);
        });
    }

    @Test
    @DisplayName("Should successfully update a address when everything is ok")
    public void testUpdateAddress() {
        Address existingAddress = new Address();
        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();
        existingAddress.setId(1L);

        when(addressRepository.findById(existingAddress.getId())).thenReturn(Optional.of(existingAddress));

        addressService.updateAddress(existingAddress.getId(), addressRequestDTO);

        verify(addressRepository, times(1)).save(existingAddress);
    }

    @Test
    @DisplayName("Should thrown a exception when address id is not found")
    public void testNotUpdateAddress() {
        Address existingAddress = new Address();
        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();
        existingAddress.setId(1L);

        when(addressRepository.findById(existingAddress.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            addressService.updateAddress(existingAddress.getId(), addressRequestDTO);
        });
    }

    @Test
    @DisplayName("Should successfully delete a address")
    public void testDeleteAddressSuccess() {
        Long addressId = 1L;

        when(addressRepository.existsById(addressId)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(addressId);

        addressService.deleteAddress(addressId);

        verify(addressRepository, times(1)).existsById(addressId);
        verify(addressRepository, times(1)).deleteById(addressId);
    }

    private AddressRequestDTO createAddressRequestDTO() {
        return new AddressRequestDTO(
                State.SP,
                "Teste",
                "Teste",
                "Teste",
                "101",
                "90000-000",
                "Teste 101",
                1L
        );
    }
}