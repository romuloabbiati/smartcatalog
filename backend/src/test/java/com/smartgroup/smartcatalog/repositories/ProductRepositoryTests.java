package com.smartgroup.smartcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.smartgroup.smartcatalog.entities.Product;

@SpringBootTest
public class ProductRepositoryTests {

	Long existingId;
	Long nonExistingId;
	
	@Autowired
	private ProductRepository productRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		// 1. Arrange -> deleteShouldDeleteObjectWhenIdExists
		existingId = 1L;
		// 1. Arrange -> deleteShouldThrowEmptyResultDataAccessExceptionWhenNonExistingId
		nonExistingId = 30L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		// 1. Arrange
		
		
		// 2. Act
		productRepository.deleteById(existingId);
		
		// 3. Assert
		Optional<Product> product = productRepository.findById(existingId);
		Assertions.assertFalse(product.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenNonExistingId() {
		
		
		// 3. Assert
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			// 1. Arrange
			
			// 2. Act
			productRepository.deleteById(nonExistingId);
		});
		
	}
	
}
