package io.luankuhlmann.ms_Customer.controllers;

import io.luankuhlmann.ms_Customer.services.CustomerService;
import io.luankuhlmann.ms_Customer.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customers")
@Tag(name = "Customer", description = "Endpoints for managing customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity registerCustomer(@Valid @RequestBody CustomerRequestDTO data) {
        return customerService.registerCustomer(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return customerService.updateCustomer(id, customerRequestDTO);
    }

    @PutMapping("/{id}/")
    public ResponseEntity updatePassword(@PathVariable Long id, @RequestParam String password) {
        return customerService.updatePassword(id, password);
    }
}
