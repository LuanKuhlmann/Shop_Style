package io.luankuhlmann.ms_Customer.service.impl;

import io.luankuhlmann.ms_Customer.services.impl.CustomerServiceImpl;
import io.luankuhlmann.ms_Customer.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.models.enums.Gender;
import io.luankuhlmann.ms_Customer.mapper.CustomerMapper;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import io.luankuhlmann.ms_Customer.exceptions.CustomerAlreadyRegisteredException;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.exceptions.InvalidCpfException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should get a customer successfully when everything is OK")
    public void testGetCustomer() {
        Customer customer = new Customer();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        when(customerMapper.mapToResponseDTO(customer)).thenReturn(new CustomerResponseDTO(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getGender(),
                customer.getCpf(),
                customer.getBirthdate(),
                customer.getEmail(),
                customer.getPassword(),
                customer.isActive(),
                customer.getAddresses()));

        ResponseEntity<CustomerResponseDTO> response = customerService.getCustomer(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer is not found")
    public void testGetCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            customerService.getCustomer(1L);
        });
    }

    @Test
    @DisplayName("Should create a customer successfully when everything is ok")
    public void testRegisterCustomer() {
        CustomerRequestDTO customerRequestDTO = createCustomerRequestDTO();
        Customer newCustomer = new Customer();
        newCustomer.setCpf("803.290.660-64");

        when(customerRepository.findByEmail(customerRequestDTO.email())).thenReturn(Optional.empty());
        when(customerMapper.mapToEntity(customerRequestDTO)).thenReturn(newCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        ResponseEntity<CustomerResponseDTO> response = customerService.registerCustomer(customerRequestDTO);
        assertNotNull(newCustomer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(customerRepository, times(1)).findByEmail(customerRequestDTO.email());
        verify(customerMapper, times(1)).mapToEntity(customerRequestDTO);
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    @DisplayName("Should return a exception when trying to create a customer when a email is already registered")
    public void testNotRegisterCustomer() {
        CustomerRequestDTO customerRequestDTO = createCustomerRequestDTO();

        when(customerRepository.findByEmail(customerRequestDTO.email())).thenReturn(Optional.of(new Customer()));

        assertThrows(CustomerAlreadyRegisteredException.class, () -> {
            customerService.registerCustomer(customerRequestDTO);
        });

        verify(customerRepository, times(1)).findByEmail(customerRequestDTO.email());
        verify(customerMapper, never()).mapToEntity(any());
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update a customer when the customer id is successfully found")
    public void testUpdateCustomer() {
        CustomerRequestDTO customerRequestDTO = createCustomerRequestDTO();
        Customer existingCustomer = mock(Customer.class);
        when(existingCustomer.getId()).thenReturn(1L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));

        ResponseEntity<CustomerResponseDTO> response = customerService.updateCustomer(1L, customerRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    @DisplayName("Should update a customer password when the customer id is successfully found")
    public void testUpdatePassword() {
        String newPassword = "newPassword";
        Customer existingCustomer = mock(Customer.class);
        when(existingCustomer.getId()).thenReturn(1L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        ResponseEntity<Void> response = customerService.updatePassword(anyLong(), newPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(customerRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    @DisplayName("Should validate a correct parsed cpf")
    public void testIsValidCPF() {
        String validCpf = "803.290.660-64";
        customerService.isValidCPF(validCpf);
    }

    @Test
    @DisplayName("Should return a error when informed a incorrect cpf")
    public void testIsInvalidCpf() {
        String invalidCpf = "123.456.789-00";
        assertThrows(InvalidCpfException.class, () -> {
            customerService.isValidCPF(invalidCpf);
        });
    }

    private CustomerRequestDTO createCustomerRequestDTO() {
        return new CustomerRequestDTO("Teste",
                "Testando",
                Gender.Masculino,
                "803.290.660-64",
                LocalDate.of(2000, 1, 1),
                "test@email.com",
                "12345678",
                true);
    }
}