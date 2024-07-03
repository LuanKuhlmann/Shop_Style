package io.luankuhlmann.ms_Customer.application.ports.in;

import io.luankuhlmann.ms_Customer.domain.dto.request.AddressRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    public ResponseEntity registerAddress(AddressRequestDTO addressRequestDTO);

    public ResponseEntity updateAddress(Long id, AddressRequestDTO addressRequestDTO);

    public void deleteAddress(Long id);
}
