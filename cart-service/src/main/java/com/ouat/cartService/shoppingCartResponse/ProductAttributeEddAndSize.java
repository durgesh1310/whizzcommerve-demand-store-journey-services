package com.ouat.cartService.shoppingCartResponse;

public class ProductAttributeEddAndSize {
	private String size;
	private String  edd;
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getEdd() {
		return edd;
	}
	public void setEdd(String string) {
		this.edd = string;
	}
	public ProductAttributeEddAndSize(String size, String edd) {
		super();
		this.size = size;
		this.edd = edd;
	}
	public ProductAttributeEddAndSize() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
