package com.microservice.app.items.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.app.items.models.entity.Item;
import com.microservice.app.items.models.entity.Product;
import com.microservice.app.items.models.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
public class ItemController {

	
	private final Logger log = LoggerFactory.getLogger(ItemController.class);
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	@Qualifier("serviceFeign")
	private IItemService itemService;
	
	@GetMapping("/lists")
	public List<Item> lists(@RequestParam(name="name", required = false) String name, @RequestHeader(name="token-request", required = false) String token){
		System.out.println(name);
		System.out.println(token);
		return itemService.findAll();
	}
	
	@GetMapping("/detail/{id}/amount/{amount}")
	public Item detail(@PathVariable Long id, @PathVariable Integer amount) {
		return cbFactory.create("items")
				.run(() -> itemService.findById(id, amount), error -> alterMethod(id, amount, error));
	}
	
	@CircuitBreaker(name="items", fallbackMethod = "alterMethod")
	@GetMapping("/detail2/{id}/amount/{amount}")
	public Item detail2(@PathVariable Long id, @PathVariable Integer amount) {
		return itemService.findById(id, amount);
	}
	
	@CircuitBreaker(name="items", fallbackMethod = "alterMethod2")
	@TimeLimiter(name="items")
	@GetMapping("/detail3/{id}/amount/{amount}")
	public CompletableFuture<Item> detail3(@PathVariable Long id, @PathVariable Integer amount) {
		return CompletableFuture.supplyAsync(() -> itemService.findById(id, amount));
	}
	
	public Item alterMethod(Long id, Integer amount, Throwable error) {
		
		log.error(error.getMessage());
		
		Item item = new Item();
		Product product = new Product();
		
		item.setAmount(amount);
		product.setId(id);
		product.setName("Alter product");
		product.setPrice(500.00);
		item.setProduct(product);
		
		return item;
	}
	
	public CompletableFuture<Item> alterMethod2(Long id, Integer amount, Throwable error) {
		
		log.error(error.getMessage());
		
		Item item = new Item();
		Product product = new Product();
		
		item.setAmount(amount);
		product.setId(id);
		product.setName("Alter product");
		product.setPrice(500.00);
		item.setProduct(product);
		
		return CompletableFuture.supplyAsync(() -> item);
	}
	
	@GetMapping("/alternative")
    public ResponseEntity<?> alternativeMethod (){
        System.out.println("debug");
        Item item = new Item();
		Product product = new Product();
		
		item.setAmount(2);
		product.setName("Alter product");
		product.setPrice(500.00);
		item.setProduct(product);
        return  ResponseEntity.ok().body(item);
    }

}
