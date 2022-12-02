package com.smartgroup.smartcatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgroup.smartcatalog.dto.ProductDTO;
import com.smartgroup.smartcatalog.services.ProductService;
import com.smartgroup.smartcatalog.services.exceptions.DatabaseException;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;
import com.smartgroup.smartcatalog.tests.TokenUtil;
import com.smartgroup.smartcatalog.tests.factories.ProductFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	private String username;
	private String password;

	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		username = "maria@gmail.com";
		password = "123456";
		
		productDTO = ProductFactory.createProductDTO();
		
		page = new PageImpl<>(List.of(productDTO));
		
		// productService.findAllPaged()
		Mockito.when(productService.findAllPaged(any(), any(), any())).thenReturn(page);
		
		// productService.findById()
		Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
		Mockito.when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		// productService.update()
		Mockito.when(productService.update(ArgumentMatchers.eq(existingId), any()))
		.thenReturn(productDTO);
		Mockito.when(productService.update(ArgumentMatchers.eq(nonExistingId), any()))
		.thenThrow(ResourceNotFoundException.class);
		
		// productService.delete()
		Mockito.doNothing().when(productService).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
		Mockito.doThrow(DatabaseException.class).when(productService).delete(dependentId);
		
		// productService.insert()
		Mockito.when(productService.insert(any())).thenReturn(productDTO);
	}
	
	@Test
	public void insertShouldReturnCreatedStatusAndProductDTO() throws Exception {
		// 1. Arrange
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		// 2. Act
		ResultActions result = mockMvc.perform(post("/products")
				.content(jsonBody)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		// 3. Assert
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void deleteShouldReturnNoContentStatusWhenIdExists() throws Exception {
		// 1. Arrange
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		// 2. Act
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		// 3. Assert
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundStatusWhenIdDoesNotExist() throws Exception {
		// 1. Arrange
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		// 2. Act
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		// 3. Assert
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		// 1. Arrange
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		// 2. Act
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		// 3. Assert
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundStatusWhenIdDoesNotExist() throws Exception {
		// 1. Arrange
		String jsonObject = objectMapper.writeValueAsString(productDTO);
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		// 2. Act
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonObject)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		// 3. Assert
		result.andExpect(status().isNotFound());
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
