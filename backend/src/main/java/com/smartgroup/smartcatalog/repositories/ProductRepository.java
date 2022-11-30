package com.smartgroup.smartcatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartgroup.smartcatalog.entities.Category;
import com.smartgroup.smartcatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("SELECT DISTINCT product FROM Product product INNER JOIN product.categories cats "
			+ "WHERE (:category IS NULL OR :category IN cats)")
	Page<Product> find(Category category, Pageable pageable);
	
}
