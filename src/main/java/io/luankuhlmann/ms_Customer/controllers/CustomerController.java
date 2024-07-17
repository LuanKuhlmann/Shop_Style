package io.luankuhlmann.ms_Customer.controllers;

import io.luankuhlmann.ms_Customer.services.CustomerService;
import io.luankuhlmann.ms_Customer.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer", description = "Endpoints for managing customers")
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> registerCustomer(@Valid @RequestBody CustomerRequestDTO data) {
        return customerService.registerCustomer(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO data) {
        return customerService.updateCustomer(id, data);
    }

    @PutMapping("/{id}/")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestParam String password) {
        return customerService.updatePassword(id, password);
    }
}
