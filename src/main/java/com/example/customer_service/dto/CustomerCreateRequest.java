package com.example.customer_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Name is required")
    private String email;

    @NotBlank(message = "Name is required")
    private String phone;
}
