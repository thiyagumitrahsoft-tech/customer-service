package com.example.customer_service.repository;


import com.example.customer_service.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long>{
    boolean existsByEmail(@NotBlank(message = "email is required") String email);

    Optional<Customer> findByEmail(String email); }
