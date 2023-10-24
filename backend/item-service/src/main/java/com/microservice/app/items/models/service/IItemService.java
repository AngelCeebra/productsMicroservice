package com.microservice.app.items.models.service;

import java.util.List;

import com.microservice.app.items.models.entity.Item;

public interface IItemService {

	public List<Item> findAll();
	
	public Item findById(Long id, Integer amount);
	
}
