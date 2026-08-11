package com.example.customer_service.controller;

import com.example.customer_service.dto.CustomerCreateRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

     private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerCreateRequest request){

        CustomerResponse response= customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id){

        CustomerResponse response=customerService.getCustomer(id);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@PathVariable String email){

        CustomerResponse response=customerService.getCustomerByEmail(email);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(response);
    }

    @PutMapping("/{id")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody CustomerCreateRequest request){

        CustomerResponse response=customerService.updateCustomer(id,request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }



}
