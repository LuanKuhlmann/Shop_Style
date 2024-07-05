package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.exceptions.CustomerAlreadyRegisteredException;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.mapper.CustomerMapper;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import io.luankuhlmann.ms_Customer.exceptions.InvalidCpfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public CustomerResponseDTO getCustomer(Long id) {
        Customer customer = getCustomerEntityById(id);

        return customerMapper.mapToResponseDTO(customer);
    }

    public ResponseEntity registerCustomer(CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.findByEmail(customerRequestDTO.email()).isPresent()) {
            throw new CustomerAlreadyRegisteredException("Customer with email " + customerRequestDTO.email() + " already registered");
        }
        Customer newCustomer = customerMapper.mapToEntity(customerRequestDTO);

        isValidCPF(newCustomer.getCpf());

        customerRepository.save(newCustomer);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = getCustomerEntityById(id);

        customerMapper.updateEntityFromDTO(customer, customerRequestDTO);
        customerRepository.save(customer);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updatePassword(Long id, String newPassword) {
        Customer customer = getCustomerEntityById(id);

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
        return ResponseEntity.ok().build();
    }

    public void isValidCPF(String cpfToValidate) {
        String cpf = cpfToValidate.replaceAll("\\D", "");

        if (cpf.length() != 11 || !cpf.matches("\\d+") || cpf.chars().distinct().count() == 1) {
            throw new InvalidCpfException("Please inform a valid CPF");
        }

        int sum = 0;

        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        int firstDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);
        sum = 0;

        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }

        int secondDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

        if (firstDigit != Character.getNumericValue(cpf.charAt(9)) || secondDigit != Character.getNumericValue(cpf.charAt(10))) {
            throw new InvalidCpfException("Please inform a valid CPF");
        }
    }

    private Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }
}