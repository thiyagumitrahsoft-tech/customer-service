package com.example.customer_service.service.serviceImpl;

import com.example.customer_service.dto.CustomerCreateRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.entity.Customer;
import com.example.customer_service.repository.CustomerRepository;
import com.example.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

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
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer Not Found with id : " + id
                ));
        return modelMapper.map(customer, CustomerResponse.class);
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

        modelMapper.map(request, customer);

        Customer savedCustomer =
                customerRepository.save(customer);

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

        customerRepository.delete(customer);
    }

}
