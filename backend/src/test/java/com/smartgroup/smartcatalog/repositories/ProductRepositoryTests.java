package com.smartgroup.smartcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smartgroup.smartcatalog.entities.Product;

@SpringBootTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		// 1. Arrange
		long existingId = 1L;
		
		// 2. Act
		productRepository.deleteById(existingId);
		
		// 3. Assert
		Optional<Product> product = productRepository.findById(existingId);
		Assertions.assertFalse(product.isPresent());
	}
	
}
