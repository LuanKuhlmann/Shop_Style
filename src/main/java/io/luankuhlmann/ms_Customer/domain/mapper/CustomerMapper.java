package io.luankuhlmann.ms_Customer.domain.mapper;

import io.luankuhlmann.ms_Customer.domain.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.domain.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerResponseDTO mapToDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSex(),
                customer.getCpf(),
                customer.getBirthdate(),
                customer.getEmail(),
                customer.getPassword(),
                customer.isActive(),
                customer.getAddresses()
        );
    }

    public Customer mapToEntity(CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerRequestDTO.firstName());
        customer.setLastName(customerRequestDTO.lastName());
        customer.setSex(customerRequestDTO.sex());
        customer.setCpf(customerRequestDTO.cpf());
        customer.setBirthdate(customerRequestDTO.birthdate());
        customer.setEmail(customerRequestDTO.email());
        customer.setActive(customerRequestDTO.active());
        customer.setPassword(passwordEncoder.encode(customerRequestDTO.password()));
        return customer;
    }

    public void updateEntityFromDTO(Customer customer, CustomerRequestDTO customerRequestDTO) {
        customer.setFirstName(customerRequestDTO.firstName());
        customer.setLastName(customerRequestDTO.lastName());
        customer.setSex(customerRequestDTO.sex());
        customer.setCpf(customerRequestDTO.cpf());
        customer.setBirthdate(customerRequestDTO.birthdate());
        customer.setEmail(customerRequestDTO.email());
        customer.setActive(customerRequestDTO.active());
    }
}