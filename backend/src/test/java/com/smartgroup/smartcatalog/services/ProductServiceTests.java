package com.smartgroup.smartcatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.smartgroup.smartcatalog.entities.Product;
import com.smartgroup.smartcatalog.repositories.CategoryRepository;
import com.smartgroup.smartcatalog.repositories.ProductRepository;
import com.smartgroup.smartcatalog.services.exceptions.DatabaseException;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;
import com.smartgroup.smartcatalog.tests.factories.ProductFactory;

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
	private Product product;
	private PageImpl<Product> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		
		nonExistingId = 2L;
		
		dependentId = 3L;
		
		product = ProductFactory.createProduct();
		
		page = new PageImpl<>(List.of(product));
		
		// Page<Product> productsPage = productRepository.findAll(pageable);
		Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		
		// entity = productRepository.save(entity);
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		// Optional<Product> product = productReporitory.findById(existingId);
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		
		// Optional<Product> product = productRepository.findById(nonExistingId);
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
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
