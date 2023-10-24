package com.microservice.app.items.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.app.items.models.entity.Item;
import com.microservice.app.items.models.entity.Product;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {
	
	@Autowired
	private RestTemplate restClient;
	
	@Override
	public List<Item> findAll() {
		List<Product> products = Arrays.asList(restClient.getForObject("http://product-service/lists", Product[].class));
		
		return products.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer amount) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		Product product = restClient.getForObject("http://product-service/detail/{id}", Product.class, pathVariables);
		
		return new Item(product, amount);
	}

}
