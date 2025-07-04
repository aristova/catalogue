package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @EntityGraph(value = "Product.reviews")
    List<Product> findAll();
}
