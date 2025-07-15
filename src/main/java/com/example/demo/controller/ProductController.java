package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.util.MessageLocalizer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ProductController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    public static final String REST_URL = "/api/products";
    private final MessageLocalizer localizer;
    private final ProductService service;

    @GetMapping
    public List<Product> all() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid Product newProduct) {
        Product saved = service.create(newProduct);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Integer id) {
        return service.find(id)
                .orElseThrow(() -> new NotFoundException(localizer.getMessage("product.not.found", id)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody @Valid Product newProduct) {
        service.update(newProduct);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);

        String cleanedFilename = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "_");

        Path destination = uploadDir.resolve(cleanedFilename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        Product product = service.find(id)
                .orElseThrow(() -> new NotFoundException(localizer.getMessage("product.not.found", id)));
        product.setImage(String.valueOf(destination));
        service.update(product);
        return ResponseEntity.ok("Uploaded to " + destination.toAbsolutePath());
    }
}
