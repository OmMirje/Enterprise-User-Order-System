package com.example.enterprise.service;

import com.example.enterprise.dto.ProductRequest;
import com.example.enterprise.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Product create(ProductRequest request);

    Product update(Long id, ProductRequest request);

    void delete(Long id);

    Product getById(Long id);

    Page<Product> list(int page, int size, String sortBy);

    Page<Product> search(String query, int page, int size, String sortBy);
}

