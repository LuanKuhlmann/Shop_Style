package io.luankuhlmann.ms_Customer.application.service;

import io.luankuhlmann.ms_Customer.application.ports.in.AddressService;
import io.luankuhlmann.ms_Customer.domain.dto.request.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.framework.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.domain.mapper.AddressMapper;
import io.luankuhlmann.ms_Customer.domain.models.Address;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import io.luankuhlmann.ms_Customer.framework.adapters.out.persistence.AddressRepository;
import io.luankuhlmann.ms_Customer.framework.adapters.out.persistence.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
