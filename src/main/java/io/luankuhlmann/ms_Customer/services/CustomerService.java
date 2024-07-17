package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    ResponseEntity<CustomerResponseDTO> getCustomer(Long id);

    ResponseEntity<CustomerResponseDTO> registerCustomer(CustomerRequestDTO customerRequestDTO);

    ResponseEntity<CustomerResponseDTO> updateCustomer(Long id, CustomerRequestDTO customerRequestDTO);

    ResponseEntity<Void> updatePassword(Long id, String newPassword);
}
