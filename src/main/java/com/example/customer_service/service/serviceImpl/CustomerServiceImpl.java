package com.example.customer_service.service.serviceImpl;

import com.example.customer_service.dto.CustomerCreateRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.entity.Customer;
import com.example.customer_service.repository.CustomerRepository;
import com.example.customer_service.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    private static final String CUSTOMER_CACHE_KEY = "customer::";

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest request) {

        if(customerRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Customer Already exists with this email");
        }
        Customer customer = modelMapper.map(request, Customer.class);

        customer.setStatus("ACTIVE");

        Customer savedCustomer = customerRepository.save(customer);

        return modelMapper.map(savedCustomer, CustomerResponse.class);

    }

    @Override
    public CustomerResponse getCustomer(Long id) {

        String key = CUSTOMER_CACHE_KEY + id;

        try {
            String cachedCustomer =
                redisTemplate.opsForValue().get(key);

        if (cachedCustomer != null) {
            return objectMapper.readValue(
                    cachedCustomer,
                    CustomerResponse.class
            );
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer Not Found with id : " + id
                ));

        CustomerResponse response =
                modelMapper.map(
                        customer,
                        CustomerResponse.class
                );

        String json =
                objectMapper.writeValueAsString(response);

        redisTemplate.opsForValue().set(
                    key,
                    json,
                Duration.ofMinutes(10)

        );

        return response;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error while processing customer cache",
                    e
            );
        }
   }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Customer Not Found with email : "+ email));
        return modelMapper.map(customer, CustomerResponse.class);
    }

    @Override
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

        String key = CUSTOMER_CACHE_KEY + id;

        redisTemplate.delete(key);

        return modelMapper.map(
                savedCustomer,
                CustomerResponse.class
        );
    }

    @Override
    public CustomerResponse updateCustomerStatus(Long id, String status) {

        Customer customer= customerRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException(
                                "No Customer found"
                        ));
        customer.setStatus(status);

        Customer savedCustomer = customerRepository.save(customer);

        redisTemplate.delete(
                CUSTOMER_CACHE_KEY + id
        );

        return modelMapper.map(
                savedCustomer,
                CustomerResponse.class
        );    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found with id: " + id
                ));

        redisTemplate.delete(
                CUSTOMER_CACHE_KEY + id
        );

        customerRepository.delete(customer);
    }

}
