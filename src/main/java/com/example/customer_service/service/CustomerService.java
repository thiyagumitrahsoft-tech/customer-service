package com.example.customer_service.service;

import com.example.customer_service.dto.CustomerCreateRequest;
import com.example.customer_service.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> getAllCustomers();

    CustomerResponse createCustomer(CustomerCreateRequest request);

    CustomerResponse getCustomer(Long id);

    CustomerResponse getCustomerByEmail(String email);

    CustomerResponse updateCustomer(Long id, CustomerCreateRequest request);

    CustomerResponse updateCustomerStatus(Long id, String status);

    void deleteCustomer(Long id);
}
