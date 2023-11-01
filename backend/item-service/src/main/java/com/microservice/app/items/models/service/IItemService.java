package com.microservice.app.items.models.service;

import java.util.List;

import com.microservice.app.items.models.entity.Item;
import com.microservice.app.items.models.entity.Product;

public interface IItemService {

	public List<Item> findAll();
	
	public Item findById(Long id, Integer amount);
	
	public Product save(Product product);
	
	public Product update(Product product, Long id);
	
	public void delete(Long id);
	
}
