package io.luankuhlmann.ms_Customer.controllers;

import io.luankuhlmann.ms_Customer.dto.response.AddressResponseDTO;
import io.luankuhlmann.ms_Customer.services.AddressService;
import io.luankuhlmann.ms_Customer.dto.request.AddressRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address", description = "Endpoints for managing address")
@RestController
@RequestMapping("/v1/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        return addressService.registerAddress(addressRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO>  updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        return addressService.updateAddress(id, addressRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        return addressService.deleteAddress(id);
    }
}