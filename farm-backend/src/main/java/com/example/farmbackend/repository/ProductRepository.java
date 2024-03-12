package com.example.farmbackend.repository;

import com.example.farmbackend.models.Employee;
import com.example.farmbackend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>  {

    Product findByName(String name);
}
