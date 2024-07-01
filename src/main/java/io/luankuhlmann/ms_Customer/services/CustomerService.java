package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.exceptions.CustomerAlreadyRegisteredException;
import io.luankuhlmann.ms_Customer.exceptions.CustomerNotFoundException;
import io.luankuhlmann.ms_Customer.mapper.CustomerMapper;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerResponseDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        return customerMapper.mapToDTO(customer);
    }

    public ResponseEntity registerCustomer(CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.findByEmail(customerRequestDTO.email()).isPresent()) {
            throw new CustomerAlreadyRegisteredException("Customer with email " + customerRequestDTO.email() + " already registered");
        }

        Customer newCustomer = customerMapper.mapToEntity(customerRequestDTO);
        customerRepository.save(newCustomer);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        customerMapper.updateEntityFromDTO(customer, customerRequestDTO);
        customerRepository.save(customer);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity updatePassword(Long id, String newPassword) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);

        return ResponseEntity.ok().build();
    }
}