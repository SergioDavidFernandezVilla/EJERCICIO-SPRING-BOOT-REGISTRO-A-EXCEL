package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Customer;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CustomRepository extends JpaRepository<Customer, Long> {
    
    Customer findByName(String name);
}
