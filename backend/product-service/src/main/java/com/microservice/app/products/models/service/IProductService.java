package com.microservice.app.products.models.service;

import java.util.List;

import com.microservice.app.products.models.entity.Product;

public interface IProductService {
	
	public List<Product> findAll();
	
	public Product findById(Long id);

}
