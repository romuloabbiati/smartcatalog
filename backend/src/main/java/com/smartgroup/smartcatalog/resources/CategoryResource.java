package com.smartgroup.smartcatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartgroup.smartcatalog.dto.CategoryDTO;
import com.smartgroup.smartcatalog.services.CategoryService;

@RestController
@RequestMapping(path = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> categoriesList = categoryService.findAll();

		return ResponseEntity.ok().body(categoriesList);
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		CategoryDTO categoryDTO = categoryService.findById(id);
		
		return ResponseEntity.ok().body(categoryDTO);
	}
	
}
