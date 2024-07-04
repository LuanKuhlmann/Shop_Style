package io.luankuhlmann.ms_Customer.domain.mapper;

import io.luankuhlmann.ms_Customer.domain.dto.request.AddressRequestDTO;
import io.luankuhlmann.ms_Customer.domain.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressRequestDTO mapToRequestDto(Address address) {
        return new AddressRequestDTO(
                address.getState(),
                address.getCity(),
                address.getDistrict(),
                address.getStreet(),
                address.getNumber(),
                address.getCep(),
                address.getComplement(),
                address.getId()
        );
    }

    public Address mapToEntity(AddressRequestDTO addressRequestDTO) {
        Address address = new Address();
        address.setState(addressRequestDTO.state());
        address.setCity(addressRequestDTO.city());
        address.setDistrict(addressRequestDTO.district());
        address.setStreet(addressRequestDTO.street());
        address.setNumber(addressRequestDTO.number());
        address.setCep(addressRequestDTO.cep());
        address.setComplement(addressRequestDTO.complement());

        return address;
    }

    public void updateEntityFromDTO(Address address, AddressRequestDTO addressRequestDTO) {
        address.setState(addressRequestDTO.state());
        address.setCity(addressRequestDTO.city());
        address.setDistrict(addressRequestDTO.district());
        address.setStreet(addressRequestDTO.street());
        address.setNumber(addressRequestDTO.number());
        address.setCep(addressRequestDTO.cep());
        address.setComplement(addressRequestDTO.complement());
    }
}
