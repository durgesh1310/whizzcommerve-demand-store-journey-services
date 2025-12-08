package com.ouat.orderService.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllocateShipperRequest {


	@JsonProperty("auth_token")
	private String authToken;

	@JsonProperty("item_name")
	private String itemName;

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

	private Integer quantity;
	@JsonProperty("invoice_value")
	private Double invoiceValue;

	@JsonProperty("cod_amount")
	private Double codAmount;

	@JsonProperty("client_order_id")
	private String clientOrderId;

	@JsonProperty("item_breadth")
	private Double itemBreadth;

	@JsonProperty("item_length")
	private Double itemLength;

	@JsonProperty("item_height")
	private Double itemHeight;

	@JsonProperty("item_weight")
	private Double itemWeight;

	@JsonProperty("is_reverse")
	private Boolean isReverse;

	@JsonProperty("invoice_number")
	private String invoiceNumber;

	@JsonProperty("total_discount")
	private Double totalDiscount;

	@JsonProperty("shipping_charge")
	private Double shippingCharge;

	@JsonProperty("transaction_charge")
	private Double transactionCharge;

	@JsonProperty("giftwrap_charge")
	private Double giftwrapCharge;

	@JsonProperty("item_list")
	private List<ItemListRequest> itemList;

}
