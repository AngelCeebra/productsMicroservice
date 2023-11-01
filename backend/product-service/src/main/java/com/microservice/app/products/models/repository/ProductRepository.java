package com.microservice.app.products.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.microservice.app.commons.models.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
