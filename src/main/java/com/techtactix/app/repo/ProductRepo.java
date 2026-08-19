package com.techtactix.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techtactix.app.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>{

}
