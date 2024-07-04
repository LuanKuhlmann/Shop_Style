package io.luankuhlmann.ms_Customer.application.service;

import io.luankuhlmann.ms_Customer.domain.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.domain.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.domain.enums.Sex;
import io.luankuhlmann.ms_Customer.domain.mapper.CustomerMapper;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import io.luankuhlmann.ms_Customer.framework.adapters.out.persistence.CustomerRepository;
import io.luankuhlmann.ms_Customer.framework.exceptions.CustomerAlreadyRegisteredException;
import io.luankuhlmann.ms_Customer.framework.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.framework.exceptions.InvalidCpfException;
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
                customer.getSex(),
                customer.getCpf(),
                customer.getBirthdate(),
                customer.getEmail(),
                customer.getPassword(),
                customer.isActive(),
                customer.getAddresses()));

        CustomerResponseDTO result = customerService.getCustomer(1L);

        assertNotNull(result);
        assertEquals(customer.getId(), result.id());
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
    public void testCreateACustomer() {
        CustomerRequestDTO customerRequestDTO = createCustomerRequestDTO();
        Customer newCustomer = new Customer();
        newCustomer.setCpf("404.624.538-71");

        when(customerRepository.findByEmail(customerRequestDTO.email())).thenReturn(Optional.empty());
        when(customerMapper.mapToEntity(customerRequestDTO)).thenReturn(newCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        ResponseEntity response = customerService.registerCustomer(customerRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(customerRepository, times(1)).findByEmail(customerRequestDTO.email());
        verify(customerMapper, times(1)).mapToEntity(customerRequestDTO);
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    @DisplayName("Should return a exception when trying to create a customer when a email is already registered")
    public void testNotCreateACustomer() {
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
    @DisplayName("Should update a customer when the customer id is successfully founded")
    public void testUpdateACustomer() {
        CustomerRequestDTO customerRequestDTO = createCustomerRequestDTO();
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));

        customerService.updateCustomer(existingCustomer.getId(), customerRequestDTO);

        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    @DisplayName("Should update a customer password when the customer id is successfully founded")
    public void testUpdatePassword() {
        String newPassword = "newPassword";
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));

        ResponseEntity<?> responseEntity = customerService.updatePassword(anyLong(), newPassword);

        verify(customerRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(newPassword);

        assertEquals(ResponseEntity.ok().build(), responseEntity);
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
        String invalidCpf = "123.456.789-00"; // Invalid CPF
        assertThrows(InvalidCpfException.class, () -> {
            customerService.isValidCPF(invalidCpf);
        });
    }

    private CustomerRequestDTO createCustomerRequestDTO() {
        return new CustomerRequestDTO("Teste",
                "Testando",
                Sex.Masculino,
                "803.290.660-64",
                LocalDate.of(2000, 1, 1),
                "test@email.com",
                "12345678",
                true);
    }
}