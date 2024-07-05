package io.luankuhlmann.ms_Customer.repositories;

import io.luankuhlmann.ms_Customer.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
