package com.smartgroup.smartcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.smartgroup.smartcatalog.repositories.CategoryRepository;
import com.smartgroup.smartcatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long existingId;
	
	private Long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		
		nonExistingId = 1000L;
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		// 3. Assert
		Assertions.assertDoesNotThrow(() -> {
			// 1. Arrange
			
			// 2. Act
			productService.delete(existingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}
	
}
