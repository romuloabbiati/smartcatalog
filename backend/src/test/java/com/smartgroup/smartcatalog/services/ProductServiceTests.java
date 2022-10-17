package com.smartgroup.smartcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.smartgroup.smartcatalog.repositories.CategoryRepository;
import com.smartgroup.smartcatalog.repositories.ProductRepository;
import com.smartgroup.smartcatalog.services.exceptions.DatabaseException;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;

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
	private Long dependentId;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		
		nonExistingId = 1000L;
		
		dependentId = 4L;
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		// 3. Assert
		Assertions.assertThrows(DatabaseException.class, () -> {
			// 1. Arrange
			
			// 2. Act
			productService.delete(dependentId);
		});
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		// 3. Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			// 1. Arrange
			
			// 2. Act
			productService.delete(nonExistingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
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
