package com.smartgroup.smartcatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartgroup.smartcatalog.dto.CategoryDTO;
import com.smartgroup.smartcatalog.entities.Category;
import com.smartgroup.smartcatalog.repositories.CategoryRepository;

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
	
}
