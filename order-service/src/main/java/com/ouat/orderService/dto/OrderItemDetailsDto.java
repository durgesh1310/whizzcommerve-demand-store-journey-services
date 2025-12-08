package com.ouat.orderService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailsDto {


	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("item_name")
	private String itemName;

	private Integer quantity;

	@JsonProperty("invoice_value")
	private Double invoiceValue;

	@JsonProperty("client_order_id")
	private String clientOrderId;

	@JsonProperty("invoice_number")
	private String invoiceNumber;
	

	@JsonProperty("cod_amount")
	private Double codAmount;

	@JsonProperty("total_discount")
	private Double totalDiscount;

	@JsonProperty("shipping_charge")
	private Double shippingCharge;

	@JsonProperty("payment_method")
	private String paymentMethod;

	@JsonProperty("to_name")
	private String toName;

	@JsonProperty("to_email")
	private String toEmail;

	@JsonProperty("to_phone_number")
	private String toPhoneNumber;

	@JsonProperty("to_pincode")
	private String toPincode;

	@JsonProperty("to_address")
	private String toAddress;

	@JsonProperty("from_name")
	private String fromName;

	@JsonProperty("from_phone_number")
	private String fromPhoneNumber;

	@JsonProperty("from_address")
	private String fromAddress;

	@JsonProperty("from_pincode")
	private String fromPincode;

	@JsonProperty("pickup_gstin")
	private String pickupGstin;

	private Double price;

	private String sku;

	@Override
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}

}