package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.exceptions.CustomerNotFoundException;
import io.luankuhlmann.ms_Customer.mapper.AddressMapper;
import io.luankuhlmann.ms_Customer.models.Address;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.AddressRepository;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + addressRequestDTO.customerId() + " not found"));

        Address newAddress = addressMapper.mapToEntity(addressRequestDTO);

        newAddress.setCustomer(customer);
        addressRepository.save(newAddress);

        return ResponseEntity.ok().build();
    }

    /*public Address updateAddress(Long id, AddressRequestDTO addressRequestDTO) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        address.setState(addressRequestDTO.state());
        address.setCity(addressRequestDTO.city());
        address.setDistrict(addressRequestDTO.district());
        address.setStreet(addressRequestDTO.street());
        address.setNumber(addressRequestDTO.number());
        address.setCep(addressRequestDTO.cep());
        address.setComplement(addressRequestDTO.complement());

        return addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
        } else {
            throw new RuntimeException("Address not found");
        }
    }*/
}
