package com.microservice.app.items.models.entity;

public class Item {

	private Product product;
	private Integer amount;
	
	
	
	public Item() {
		
	}

	public Item(Product product, Integer amount) {
		super();
		this.product = product;
		this.amount = amount;
	}

	public Double getTotal() {
		return product.getPrice() * amount.doubleValue();
	}
	
	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


	public Integer getAmount() {
		return amount;
	}


	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
