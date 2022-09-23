package com.smartgroup.smartcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartgroup.smartcatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
