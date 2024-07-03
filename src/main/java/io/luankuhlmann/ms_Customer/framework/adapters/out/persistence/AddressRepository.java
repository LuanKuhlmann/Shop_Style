package io.luankuhlmann.ms_Customer.framework.adapters.out.persistence;

import io.luankuhlmann.ms_Customer.domain.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
