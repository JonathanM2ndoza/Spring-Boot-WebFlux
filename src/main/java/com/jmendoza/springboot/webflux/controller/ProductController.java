package com.jmendoza.springboot.webflux.controller;

import com.jmendoza.springboot.webflux.model.Product;
import com.jmendoza.springboot.webflux.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public Mono<Product> create(@Valid @RequestBody Product product) {
        return productService.create(product);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findById(@PathVariable(value = "id") int id) {
        return productService.findById(id);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> findAll() {
        return productService.findAll();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> update(@PathVariable(value = "id") int id,
                                                @Valid @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteById(@PathVariable(value = "id") int id) {
        return productService.deleteById(id);
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1)).take(15)
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }
}
