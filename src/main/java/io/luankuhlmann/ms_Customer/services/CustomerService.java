package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    CustomerResponseDTO getCustomer(Long id);

    ResponseEntity registerCustomer(CustomerRequestDTO customerRequestDTO);

    ResponseEntity updateCustomer(Long id, CustomerRequestDTO customerRequestDTO);

    ResponseEntity updatePassword(Long id, String newPassword);
}
