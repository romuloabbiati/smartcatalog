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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.smartgroup.smartcatalog.dto.ProductDTO;
import com.smartgroup.smartcatalog.entities.Category;
import com.smartgroup.smartcatalog.entities.Product;
import com.smartgroup.smartcatalog.repositories.CategoryRepository;
import com.smartgroup.smartcatalog.repositories.ProductRepository;
import com.smartgroup.smartcatalog.services.exceptions.DatabaseException;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;
import com.smartgroup.smartcatalog.tests.factories.CategoryFactory;
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
	private Category category;
	private PageImpl<Product> page;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		
		nonExistingId = 2L;
		
		dependentId = 3L;
		
		product = ProductFactory.createProduct();
		
		category = CategoryFactory.createCategory();
		
		productDTO = ProductFactory.createProductDTO();
		
		page = new PageImpl<>(List.of(product));
		
		// Page<ProductDTO> productsDTOPage = productRepository.findAll(pageable);
		Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		
		// entity = productRepository.save(entity);
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		// Optional<Product> productOptional = productRepository.findById(existingId);
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		
		// Optional<Product> productOptional = productRepository.findById(nonExistingId);
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		// Product product = productRepository.getOne(existingId);
		Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
		
		// Product product = productRepository.getOne(nonExistingId);
		Mockito.when(productRepository.getOne(nonExistingId)).thenThrow(EmptyResultDataAccessException.class);
		
		// Category category = categoryRepository.getOne(existingId);
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		
		// Category category = categoryRepository.getOne(nonExistingId);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EmptyResultDataAccessException.class);
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundException() {
		// 3. Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			// 1. Arrange
			
			// 2. Act
			productDTO = productService.update(nonExistingId, productDTO);
		});
		Mockito.verify(productRepository, Mockito.times(1)).getOne(nonExistingId);
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		// 1. Arrange
		
		// 2. Act
		productDTO = productService.update(existingId, productDTO);
		
		// 3. Assert
		Assertions.assertNotNull(productDTO);
		Mockito.verify(productRepository, Mockito.times(1)).getOne(existingId);
	}
	
	@Test
	public void findByIdShouldReturnOptionalWithProductDTOWhenIdExists() {
		// 1. Arrange
		
		// 2. Act
		ProductDTO productDTO = productService.findById(existingId);
		// 3. Assert
		Assertions.assertNotNull(productDTO);
		Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
		// 3. Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			// 1. Arrange
			
			// 2. Act
			ProductDTO productDTO = productService.findById(nonExistingId);
		});
		Mockito.verify(productRepository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		// 1. Arrange
		Pageable pageable = PageRequest.of(0, 10);
		// 2. Act
		Page<ProductDTO> productDTOPage = productService.findAllPaged(pageable);
		
		// 3. Assert
		Assertions.assertNotNull(productDTOPage);
		Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
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
