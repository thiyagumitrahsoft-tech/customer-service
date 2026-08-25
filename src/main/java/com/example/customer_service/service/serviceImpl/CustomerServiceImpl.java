package com.example.customer_service.service.serviceImpl;

import com.example.customer_service.dto.CustomerCreateRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.entity.Customer;
import com.example.customer_service.repository.CustomerRepository;
import com.example.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    @Override
    @Cacheable(cacheNames = "customerList", key = "'all'")
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponse> response = customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "customerList", allEntries = true),
            @CacheEvict(cacheNames = "customerByEmail", allEntries = true)
    })
    public CustomerResponse createCustomer(CustomerCreateRequest request) {

        if(customerRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Customer Already exists with this email");
        }
        Customer customer = modelMapper.map(request, Customer.class);

        customer.setStatus("ACTIVE");

        Customer savedCustomer = customerRepository.save(customer);
        CustomerResponse response = modelMapper.map(savedCustomer, CustomerResponse.class);

        return response;

    }

    @Override
    @Cacheable(cacheNames = "customer", key = "#id")
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer Not Found with id : " + id
                ));

        CustomerResponse response =
                modelMapper.map(
                        customer,
                        CustomerResponse.class
                );

        return response;
   }

    @Override
    @Cacheable(cacheNames = "customerByEmail", key = "#email")
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Customer Not Found with email : "+ email));
        CustomerResponse response = modelMapper.map(customer, CustomerResponse.class);
        return response;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "customer", key = "#id"),
            @CacheEvict(cacheNames = "customerList", allEntries = true),
            @CacheEvict(cacheNames = "customerByEmail", allEntries = true)
    })
    public CustomerResponse updateCustomer(Long id, CustomerCreateRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + id
                        )
                );

        if (!Objects.equals(customer.getEmail(), request.getEmail())) {
            customerRepository.findByEmail(request.getEmail())
                    .ifPresent(existingCustomer -> {
                        throw new RuntimeException(
                                "Customer already exists with email: " + request.getEmail()
                        );
                    });

            customer.setEmail(request.getEmail());
        }

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());

        Customer savedCustomer =
                customerRepository.save(customer);

        CustomerResponse response = modelMapper.map(
                savedCustomer,
                CustomerResponse.class
        );
        return response;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "customer", key = "#id"),
            @CacheEvict(cacheNames = "customerList", allEntries = true)
    })
    public CustomerResponse updateCustomerStatus(Long id, String status) {

        Customer customer= customerRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException(
                                "No Customer found"
                        ));
        customer.setStatus(status);

        Customer savedCustomer = customerRepository.save(customer);

        CustomerResponse response = modelMapper.map(
                savedCustomer,
                CustomerResponse.class
        );
        return response;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "customer", key = "#id"),
            @CacheEvict(cacheNames = "customerList", allEntries = true),
            @CacheEvict(cacheNames = "customerByEmail", allEntries = true)
    })
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found with id: " + id
                ));

        customerRepository.delete(customer);
    }

}
