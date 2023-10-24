package com.microservice.app.products.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.app.products.models.entity.Product;
import com.microservice.app.products.models.service.IProductService;

@RestController
public class ProductController {
	
	@Autowired
    private Environment environment;
	
	@Autowired
	private IProductService productService;
	
	@GetMapping("/lists")
	public List<Product> getAllProducts() {
		return productService.findAll().stream().map(product -> {
			product.setServerPort(Integer.parseInt(environment.getProperty("local.server.port")));
			return product;
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/detail/{id}")
	public Product getProduct(@PathVariable Long id) throws InterruptedException {
		
		if(id.equals(10L)) {
			throw new IllegalStateException("Product does not find!");
		}
		
		if(id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5L);
		}
		
		Product product = productService.findById(id);
		product.setServerPort(Integer.parseInt(environment.getProperty("local.server.port")));
		return product;
	}
	
}
