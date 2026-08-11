package com.example.customer_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false, unique = false, length = 100)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="phone", length = 20)
    private String phone;

    @Column(name="status", nullable= false ,length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        ZoneId indiaZone = ZoneId.of("IST");
        LocalDateTime now= LocalDateTime.now(indiaZone);

        createdAt= now;
        updatedAt= now;

        if(status == null){
            status = "ACTIVE";
        }
    }

    @PreUpdate
    public void preUpdate(){
        ZoneId indiaZone = ZoneId.of("IST");
        updatedAt = LocalDateTime.now(indiaZone);
    }

}
