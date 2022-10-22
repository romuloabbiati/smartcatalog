package com.smartgroup.smartcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smartgroup.smartcatalog.repositories.ProductRepository;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceINT {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		// 1. Arrange
		
		// 2. Act
		productService.delete(existingId);
		
		// 3. Assert
		Assertions.assertEquals(countTotalProducts - 1L, productRepository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		// 3. Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			// 1. Arrange
			
			// 2. Act
			productService.delete(nonExistingId);
		});
	}
	
}
