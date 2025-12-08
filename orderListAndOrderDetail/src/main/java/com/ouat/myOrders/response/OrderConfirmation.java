package com.ouat.myOrders.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderConfirmation {
	private String bannerImage;
	private Integer orderNumber;
	private String customOrderNumber;
	private String orderDate;
	private List<OrderItemDetailForOrderConfirmation> orderItem;
	private PriceSummary priceSummary;
	private Address shippingAddress;
	private String campaignUrl;
	
	
	public String getBannerImage() {
		return bannerImage;
	}
	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getCustomOrderNumber() {
        return customOrderNumber;
    }
    public void setCustomOrderNumber(String customOrderNumber) {
        this.customOrderNumber = customOrderNumber;
    }
	
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String string) {
		this.orderDate = string;
	}
	public List<OrderItemDetailForOrderConfirmation> getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(List<OrderItemDetailForOrderConfirmation> orderItem) {
		this.orderItem = orderItem;
	}
	public PriceSummary getPriceSummary() {
		return priceSummary;
	}
	public void setPriceSummary(PriceSummary priceSummary) {
		this.priceSummary = priceSummary;
	}
	public Address getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
 
	public String getCampaignUrl() {
		return campaignUrl;
	}
	public void setCampaignUrl(String campaignUrl) {
		this.campaignUrl = campaignUrl;
	}

    public OrderConfirmation(String bannerImage, String customOrderNumber, Integer orderNumber,
            String orderDate, List<OrderItemDetailForOrderConfirmation> orderItem,
            PriceSummary priceSummary, Address shippingAddress, String campaignUrl) {
        super();
        this.bannerImage = bannerImage;
        this.orderNumber = orderNumber;
        this.customOrderNumber = customOrderNumber;
        this.orderDate = orderDate;
        this.orderItem = orderItem;
        this.priceSummary = priceSummary;
        this.shippingAddress = shippingAddress;
        this.campaignUrl = campaignUrl;
    }
	
	@Override
	public String toString() {
		return "OrderConfirmation [bannerImage=" + bannerImage + ", orderNumber=" + orderNumber + ", orderDate="
				+ orderDate + ", orderItem=" + orderItem + ", priceSummary=" + priceSummary + ", shippingAddress="
				+ shippingAddress + ", campaignUrl=" + campaignUrl + "]";
	}
	public OrderConfirmation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
