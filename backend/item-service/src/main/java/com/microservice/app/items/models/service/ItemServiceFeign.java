package com.microservice.app.items.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.app.items.clients.ProductClientRest;
import com.microservice.app.items.models.entity.Item;

@Service("serviceFeign")
public class ItemServiceFeign implements IItemService {

	@Autowired
	private ProductClientRest clientFeign;
	
	@Override
	public List<Item> findAll() {
		return clientFeign.list().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer amount) {
		return new Item(clientFeign.getProduct(id), amount);
	}

}