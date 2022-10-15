package com.smartgroup.smartcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.smartgroup.smartcatalog.entities.Product;
import com.smartgroup.smartcatalog.tests.factories.ProductFactory;

@SpringBootTest
public class ProductRepositoryTests {

	Long existingId;
	Long nonExistingId;
	Long totalProductsCount;
	
	@Autowired
	private ProductRepository productRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		// 1. Arrange -> deleteShouldDeleteObjectWhenIdExists
		existingId = 1L;
		// 1. Arrange -> deleteShouldThrowEmptyResultDataAccessExceptionWhenNonExistingId
		nonExistingId = 30L;
		// 1. Arrange -> saveShouldPersistWithAutoIncrementWhenIdIsNull
		totalProductsCount = 25L;
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
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		// 1. Arrange
		Product product = ProductFactory.createProduct();
		product.setId(null);
		
		// 2. Act
		product = productRepository.save(product);
		
		// 3. Assert
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(totalProductsCount + 1L, product.getId());
		
	}
	
	@Test
	public void findByIdShouldReturnOptionalNotEmptyWhenIdExists() {
		// 1. Arrange
		
		// 2. Act
		Optional<Product> productOptional = productRepository.findById(existingId);
		// 3. Assert
		Assertions.assertTrue(productOptional.isPresent());
		Assertions.assertFalse(productOptional.isEmpty());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdNotExist() {
		// 1. Arrange
		
		// 2. Act
		Optional<Product> productOptional = productRepository.findById(nonExistingId);
		
		// 3. Assert
		Assertions.assertTrue(productOptional.isEmpty());
		Assertions.assertFalse(productOptional.isPresent());
	}
	
}
