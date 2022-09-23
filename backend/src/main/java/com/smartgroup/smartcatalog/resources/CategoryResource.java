package com.smartgroup.smartcatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartgroup.smartcatalog.entities.Category;

@RestController
@RequestMapping(path = "/categories")
public class CategoryResource {

	public ResponseEntity<List<Category>> findAll() {
		List<Category> categories = new ArrayList<>();
		
		categories.add(new Category(1L, "Books"));
		categories.add(new Category(2L, "Laptops"));
		
		return ResponseEntity.ok().body(categories);
	}
	
}
