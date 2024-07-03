package io.luankuhlmann.ms_Customer.framework.adapters.out.persistence;

import io.luankuhlmann.ms_Customer.domain.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
}