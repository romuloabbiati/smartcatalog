package com.smartgroup.smartcatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartgroup.smartcatalog.dto.ProductDTO;
import com.smartgroup.smartcatalog.entities.Product;
import com.smartgroup.smartcatalog.repositories.ProductRepository;
import com.smartgroup.smartcatalog.services.exceptions.DatabaseException;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> productsPage = productRepository.findAll(pageRequest);
		return productsPage.map(product -> new ProductDTO(product, product.getCategories()));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> object = productRepository.findById(id);
		Product product = object
				.orElseThrow(() -> new EntityNotFoundException("Entity not found!"));
		
		ProductDTO productDTO = new ProductDTO(product, product.getCategories());
		return productDTO;
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setImgUrl(productDTO.getImgUrl());
		product.setDate(productDTO.getDate());
		
		product = productRepository.save(product);
		
		return new ProductDTO(product);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product product = productRepository.getOne(id);
			product.setName(productDTO.getName());
			product.setDescription(productDTO.getDescription());
			product.setPrice(productDTO.getPrice());
			product.setImgUrl(productDTO.getImgUrl());
			product.setDate(productDTO.getDate());
			
			product = productRepository.save(product);
			
			return new ProductDTO(product);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
	}
	
}
