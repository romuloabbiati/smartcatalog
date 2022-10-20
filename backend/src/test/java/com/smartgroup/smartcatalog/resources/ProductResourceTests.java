package com.smartgroup.smartcatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.smartgroup.smartcatalog.dto.ProductDTO;
import com.smartgroup.smartcatalog.services.ProductService;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;
import com.smartgroup.smartcatalog.tests.factories.ProductFactory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
	private Long existingId;
	private Long nonExistingId;

	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		
		productDTO = ProductFactory.createProductDTO();
		
		page = new PageImpl<>(List.of(productDTO));
		
		Mockito.when(productService.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
		
		Mockito.when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		// 1. Arrange
		
		// 2. Act
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		// 3. Assert
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOAndStatusOkWhenIdExists() throws Exception {
		// 1. Arrange
		
		// 2. Act
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		// 3. Assert
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath(".name").exists());
		result.andExpect(jsonPath(".description").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		// 1. Arrange
		
		// 2. Act
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		// 3. Assert
		result.andExpect(status().isNotFound());
	}
	
}
