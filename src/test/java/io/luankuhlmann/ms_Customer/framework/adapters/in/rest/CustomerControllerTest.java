package io.luankuhlmann.ms_Customer.framework.adapters.in.rest;

import io.luankuhlmann.ms_Customer.domain.dto.request.CustomerRequestDTO;
import io.luankuhlmann.ms_Customer.domain.models.Customer;
import io.luankuhlmann.ms_Customer.framework.adapters.out.persistence.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerControllerTest {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get customer successfully from DB")
    void findCustomerById() {
        CustomerRequestDTO data = new CustomerRequestDTO("Teste",
                "Testando",
                "Feminino",
                "404.624.538.71",
                LocalDate.of(2000, 1, 1),
                "teste@email.com",
                "12345678",
                true);

        Customer createdCustomer = this.createCustomer(data);

        Optional<Customer> result = this.customerRepository.findById(createdCustomer.getId());

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get Customer from DB when customer not exists")
    void doesNotFindCustomerById() {
        Optional<Customer> result = this.customerRepository.findById(1L);

        assertThat(result.isEmpty()).isTrue();
    }

    private Customer createCustomer(CustomerRequestDTO data) {
        Customer newCustomer = new Customer();
        newCustomer.setCpf(data.cpf());
        newCustomer.setFirstName(data.firstName());
        newCustomer.setLastName(data.lastName());
        newCustomer.setSex(data.sex());
        newCustomer.setBirthdate(data.birthdate());
        newCustomer.setEmail(data.email());
        newCustomer.setPassword(data.password());
        newCustomer.setActive(data.active());

        this.entityManager.persist(newCustomer);
        this.entityManager.flush();

        return newCustomer;
    }
}