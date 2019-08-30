package com.jmendoza.springboot.webflux.service;

import com.jmendoza.springboot.webflux.model.Product;
import com.jmendoza.springboot.webflux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Mono<Product> create(Product product) {
        return productRepository.insert(product);
    }

    public Mono<ResponseEntity<Product>> findById(int id) {
        return productRepository.findById(id).map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<ResponseEntity<Product>> update(int id, Product product) {
        return productRepository.findById(id)
                .flatMap(product1 -> {
                    product1.setDescription(product.getDescription());
                    return productRepository.save(product1);
                })
                .map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<String>> deleteById(int id) {
        return productRepository.findById(id)
                .flatMap(product ->
                        productRepository.delete(product)
                                .then(Mono.just(new ResponseEntity<>("Product Delete", HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND));
    }
}
