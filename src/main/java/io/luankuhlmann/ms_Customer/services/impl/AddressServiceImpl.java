package io.luankuhlmann.ms_Customer.services.impl;

import io.luankuhlmann.ms_Customer.dto.request.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.dto.response.AddressResponseDTO;
import io.luankuhlmann.ms_Customer.dto.response.CustomerResponseDTO;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.mapper.AddressMapper;
import io.luankuhlmann.ms_Customer.models.Address;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.AddressRepository;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import io.luankuhlmann.ms_Customer.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressMapper addressMapper;

    public ResponseEntity<AddressResponseDTO> registerAddress(AddressRequestDTO addressRequestDTO) {
        Customer customer = findCustomerById(addressRequestDTO);
        Address newAddress = addressMapper.mapToEntity(addressRequestDTO);

        newAddress.setCustomer(customer);

        AddressResponseDTO createdAddress = addressMapper.mapToResponseDTO(addressRepository.save(newAddress));
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    public ResponseEntity<AddressResponseDTO> updateAddress(Long id, AddressRequestDTO addressRequestDTO) {
        Address address = findAddressById(id);

        addressMapper.updateEntityFromDTO(address, addressRequestDTO);

        AddressResponseDTO updatedAddress = addressMapper.mapToResponseDTO(addressRepository.save(address));
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("Address with id " + id + " not found");
        }
    }

    private Customer findCustomerById(AddressRequestDTO addressRequestDTO) {
        return customerRepository.findById(addressRequestDTO.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + addressRequestDTO.customerId() + " not found"));
    }

    private Address findAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address with id " + id + " not found"));
    }
}
