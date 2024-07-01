package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.mapper.AddressMapper;
import io.luankuhlmann.ms_Customer.models.Address;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.AddressRepository;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressMapper addressMapper;

    public ResponseEntity registerAddress(AddressRequestDTO addressRequestDTO) {
        Customer customer = customerRepository.findById(addressRequestDTO.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + addressRequestDTO.customerId() + " not found"));

        Address newAddress = addressMapper.mapToEntity(addressRequestDTO);

        newAddress.setCustomer(customer);
        addressRepository.save(newAddress);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateAddress(Long id, AddressRequestDTO addressRequestDTO) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address with id " + id + " not found"));

        addressMapper.updateEntityFromDTO(address, addressRequestDTO);
        addressRepository.save(address);

        return ResponseEntity.ok().build();
    }

    public void deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Address with id " + id + " not found");
        }
    }
}
