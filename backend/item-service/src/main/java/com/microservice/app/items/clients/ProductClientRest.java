package com.microservice.app.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservice.app.items.models.entity.Product;

@FeignClient(name="product-service")
public interface ProductClientRest {

	@GetMapping("/lists")
	public List<Product> list();
	
	@GetMapping("/detail/{id}")
	public Product getProduct(@PathVariable Long id);
	
}
