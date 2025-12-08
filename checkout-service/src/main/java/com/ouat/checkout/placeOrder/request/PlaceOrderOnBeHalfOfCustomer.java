package com.ouat.checkout.placeOrder.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.checkout.cache.OrderedItemCache;
import com.ouat.checkout.dto.AddressDto;
import com.ouat.checkout.dto.PricingSummaryDto;
import com.ouat.checkout.enums.PaymentMode;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlaceOrderOnBeHalfOfCustomer {
	 private Integer addressId;
	    private List<OrderedItemCache> orderSummary;
	    private PricingSummaryDto pricingSummary;
	    private String orderConfirmationEmail;
	    private Map<String, Double> creditApplied;
	    private String promoCode;
	    private List<PaymentMode> paymentMode;
	    private AddressDto address;
		public Integer getAddressId() {
			return addressId;
		}
		public void setAddressId(Integer addressId) {
			this.addressId = addressId;
		}
		public List<OrderedItemCache> getOrderSummary() {
			return orderSummary;
		}
		public void setOrderSummary(List<OrderedItemCache> orderSummary) {
			this.orderSummary = orderSummary;
		}
		public PricingSummaryDto getPricingSummary() {
			return pricingSummary;
		}
		public void setPricingSummary(PricingSummaryDto pricingSummary) {
			this.pricingSummary = pricingSummary;
		}
		public String getOrderConfirmationEmail() {
			return orderConfirmationEmail;
		}
		public void setOrderConfirmationEmail(String orderConfirmationEmail) {
			this.orderConfirmationEmail = orderConfirmationEmail;
		}
		public Map<String, Double> getCreditApplied() {
			return creditApplied;
		}
		public void setCreditApplied(Map<String, Double> creditApplied) {
			this.creditApplied = creditApplied;
		}
		public String getPromoCode() {
			return promoCode;
		}
		public void setPromoCode(String promoCode) {
			this.promoCode = promoCode;
		}
		public List<PaymentMode> getPaymentMode() {
			return paymentMode;
		}
		public void setPaymentMode(List<PaymentMode> paymentMode) {
			this.paymentMode = paymentMode;
		}
		public AddressDto getAddress() {
			return address;
		}
		public void setAddress(AddressDto address) {
			this.address = address;
		}
		public PlaceOrderOnBeHalfOfCustomer(Integer addressId, List<OrderedItemCache> orderSummary,
				PricingSummaryDto pricingSummary, String orderConfirmationEmail, Map<String, Double> creditApplied,
				String promoCode, List<PaymentMode> paymentMode, AddressDto address) {
			super();
			this.addressId = addressId;
			this.orderSummary = orderSummary;
			this.pricingSummary = pricingSummary;
			this.orderConfirmationEmail = orderConfirmationEmail;
			this.creditApplied = creditApplied;
			this.promoCode = promoCode;
			this.paymentMode = paymentMode;
			this.address = address;
		}
		public PlaceOrderOnBeHalfOfCustomer() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    

}
