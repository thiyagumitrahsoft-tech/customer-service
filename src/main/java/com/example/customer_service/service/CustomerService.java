package com.example.customer_service.service;

import com.example.customer_service.dto.CustomerCreateRequest;
import com.example.customer_service.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest request);

    CustomerResponse getCustomer(Long id);

    CustomerResponse getCustomerByEmail(String email);

    CustomerResponse updateCustomer(Long id, CustomerCreateRequest request);
}
