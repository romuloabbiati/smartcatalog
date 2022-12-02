package com.smartgroup.smartcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.smartgroup.smartcatalog.dto.ProductDTO;
import com.smartgroup.smartcatalog.repositories.ProductRepository;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
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
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0AndSize10() {
		// 1. Arrange
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		// 2. Act
		Page<ProductDTO> result = productService.findAllPaged(0L, "", pageRequest);
		
		// 3. Assert
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
		
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		// 1. Arrange
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		// 2. Act
		Page<ProductDTO> result = productService.findAllPaged(0L, "", pageRequest);
		
		// 3. Assert
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortedByName() {
		// 1. Arrange
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		// 2. Act
		Page<ProductDTO> result = productService.findAllPaged(0L, "", pageRequest);
		
		// 3. Assert
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
	
}
