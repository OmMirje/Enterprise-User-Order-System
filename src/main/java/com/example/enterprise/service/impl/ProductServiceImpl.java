package com.example.enterprise.service.impl;

import com.example.enterprise.dto.ProductRequest;
import com.example.enterprise.entity.Product;
import com.example.enterprise.exception.ResourceNotFoundException;
import com.example.enterprise.repository.ProductRepository;
import com.example.enterprise.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product product = getById(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = getById(id);
        productRepository.delete(product);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    public Page<Product> list(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> search(String query, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }
}

