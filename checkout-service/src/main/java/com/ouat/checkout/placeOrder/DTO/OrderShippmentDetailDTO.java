
package com.ouat.checkout.placeOrder.DTO;

public class OrderShippmentDetailDTO {
	private Integer orderId;
	private Integer addressId;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public OrderShippmentDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderShippmentDetailDTO(Integer orderId, Integer addressId) {
		super();
		this.orderId = orderId;
		this.addressId = addressId;
	}
	
}
