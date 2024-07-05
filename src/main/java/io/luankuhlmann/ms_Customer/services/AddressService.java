package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.request.AddressRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    ResponseEntity registerAddress(AddressRequestDTO addressRequestDTO);

    ResponseEntity updateAddress(Long id, AddressRequestDTO addressRequestDTO);

    void deleteAddress(Long id);
}
