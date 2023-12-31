package com.microservice.app.items.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.app.items.models.entity.Item;
import com.microservice.app.commons.models.entity.Product;
import com.microservice.app.items.models.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RefreshScope
//POST url: ip:port/actuator/refresh
//refresh changes in config server
@RestController
public class ItemController {

	
	private final Logger log = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private Environment env;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	@Qualifier("serviceFeign")
	private IItemService itemService;
	
	@Value("${config.text}")
	private String text;
	
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

	@GetMapping("/get-config")
	public  ResponseEntity<?> getConfig(@Value("${server.port}") String port) {
		
		log.info(text);
		
		Map<String, String> json = new HashMap<>();
		json.put("text", text);
		json.put("port", port);
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("author.name", env.getProperty("config.author.name"));
			json.put("author.email", env.getProperty("config.author.email"));
		}
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Product create(@RequestBody Product product) {
		return itemService.save(product);
	}
	
	@PutMapping("/update/{id}") 
	@ResponseStatus(HttpStatus.CREATED)
	public Product update(@RequestBody Product product, @PathVariable Long id){
		return itemService.update(product, id);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		itemService.delete(id);
	}
}
