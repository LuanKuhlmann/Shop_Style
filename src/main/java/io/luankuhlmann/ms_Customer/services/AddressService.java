package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.request.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.AddressResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    ResponseEntity<AddressResponseDTO>  registerAddress(AddressRequestDTO addressRequestDTO);

    ResponseEntity<AddressResponseDTO> updateAddress(Long id, AddressRequestDTO addressRequestDTO);

    ResponseEntity<Void> deleteAddress(Long id);
}
