package com.ouat.checkout.placeOrder.DTO;

import java.sql.Date;

import com.ouat.checkout.response.Platform;

public class OrderDTO {
	
	
	private Integer customerId;
	private Integer orderStatusId;
	private String orderNumber;
	private Double totalAmount;
	private Double orderPayable;
	private Double platformOfferedDiscount;
	private Double promoDiscount;
	private Double creditApplied;
	private Double shippingCharges;
	private Date orderDate;
	private String orderType;
	private Date cartCreatedDate;
	private String paymentMethod;
	private String promocode;
	private String platform ;
	private String utmSource;
	private String utmCampaign;
	private String utmMedium;
	
	 

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUtmSource() {
		return utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public String getUtmCampaign() {
		return utmCampaign;
	}

	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}

	public String getUtmMedium() {
		return utmMedium;
	}

	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getOrderPayable() {
		return orderPayable;
	}

	public void setOrderPayable(Double orderPayable) {
		this.orderPayable = orderPayable;
	}

	public Double getPlatformOfferedDiscount() {
		return platformOfferedDiscount;
	}

	public void setPlatformOfferedDiscount(Double platformOfferedDiscount) {
		this.platformOfferedDiscount = platformOfferedDiscount;
	}

	public Double getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(Double promoDiscount) {
		this.promoDiscount = promoDiscount;
	}

	public Double getCreditApplied() {
		return creditApplied;
	}

	public void setCreditApplied(Double creditApplied) {
		this.creditApplied = creditApplied;
	}

	public Double getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(Double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getCartCreatedDate() {
		return cartCreatedDate;
	}

	public void setCartCreatedDate(Date cartCreatedDate) {
		this.cartCreatedDate = cartCreatedDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPromocode() {
		return promocode;
	}

	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}

	public OrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
 
	 
	public OrderDTO(Integer customerId, Integer orderStatusId, Double totalAmount, Double orderPayable,
			Double platformOfferedDiscount, Double promoDiscount, Double creditApplied, Double shippingCharges,
			Date orderDate, String orderType, Date cartCreatedDate, String paymentMethod, String promocode,
			String platform, String utmSource, String utmCampaign, String utmMedium) {
		super();
		this.customerId = customerId;
		this.orderStatusId = orderStatusId;
		this.totalAmount = totalAmount;
		this.orderPayable = orderPayable;
		this.platformOfferedDiscount = platformOfferedDiscount;
		this.promoDiscount = promoDiscount;
		this.creditApplied = creditApplied;
		this.shippingCharges = shippingCharges;
		this.orderDate = orderDate;
		this.orderType = orderType;
		this.cartCreatedDate = cartCreatedDate;
		this.paymentMethod = paymentMethod;
		this.promocode = promocode;
		this.platform = platform;
		this.utmSource = utmSource;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
	}

	@Override
	public String toString() {
		return "OrderDTO [customerId=" + customerId + ", orderStatusId=" + orderStatusId + ", totalAmount="
				+ totalAmount + ", orderPayable=" + orderPayable + ", platforOfferedDiscount=" + platformOfferedDiscount
				+ ", creditApplied=" + creditApplied + ", shippingCharges=" + shippingCharges + ", orderDate="
				+ orderDate + ", orderType=" + orderType + ", cartCreatedDate=" + cartCreatedDate + ", paymentMethod="
				+ paymentMethod + ", promocode=" + promocode + "]";
	}

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
	
}
