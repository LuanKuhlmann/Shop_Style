package io.luankuhlmann.ms_Customer.application.ports.in;

import io.luankuhlmann.ms_Customer.domain.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.domain.dto.response.CustomerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    public CustomerResponseDTO getCustomer(Long id);

    public ResponseEntity registerCustomer(CustomerRequestDTO customerRequestDTO);

    public ResponseEntity updateCustomer(Long id, CustomerRequestDTO customerRequestDTO);

    public ResponseEntity updatePassword(Long id, String newPassword);
}
