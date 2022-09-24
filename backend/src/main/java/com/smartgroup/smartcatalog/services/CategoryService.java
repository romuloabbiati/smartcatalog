package com.smartgroup.smartcatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartgroup.smartcatalog.dto.CategoryDTO;
import com.smartgroup.smartcatalog.entities.Category;
import com.smartgroup.smartcatalog.repositories.CategoryRepository;
import com.smartgroup.smartcatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryRepository.findAll();
		
		return categories.stream()
			.map(category -> new CategoryDTO(category))
			.collect(Collectors.toList());
		
//		List<CategoryDTO> categoriesDTO = new ArrayList<>();
//		for(Category category : categories) {
//			categoriesDTO.add(new CategoryDTO(category));
//		}
	}

	public CategoryDTO findById(Long id) {
		Optional<Category> object = categoryRepository.findById(id);
		
		Category category = object
				.orElseThrow(() -> new EntityNotFoundException("Entity not Found!"));
		
		CategoryDTO categoryDTO = new CategoryDTO(category);
		
		return categoryDTO;
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setName(categoryDTO.getName());
		category = categoryRepository.save(category);
		
		return new CategoryDTO(category);
	}
	
	
	
}
