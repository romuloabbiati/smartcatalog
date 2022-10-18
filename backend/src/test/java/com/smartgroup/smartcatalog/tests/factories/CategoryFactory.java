package com.smartgroup.smartcatalog.tests.factories;

import com.smartgroup.smartcatalog.entities.Category;

public class CategoryFactory {

	public static Category createCategory() {
		return new Category(2L, "Eletrônicos");
	}
}
