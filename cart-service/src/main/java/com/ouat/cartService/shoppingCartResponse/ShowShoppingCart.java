package com.ouat.cartService.shoppingCartResponse;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowShoppingCart implements Serializable{
 
     /**
	 * 
	 */
	private static final long serialVersionUID = -8401487436137362381L;
	/**
	 * 
	 */
 	
	 Integer totalItem=0;
	 List<ShowShoppingCartItemResponse> showShoppingCartData;
	 ShoppingCartPriceSummaryResponse  showShoppingCartSummary;
	public Integer getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;
	}
	public List<ShowShoppingCartItemResponse> getShowShoppingCartData() {
		return showShoppingCartData;
	}
	public void setShowShoppingCartData(List<ShowShoppingCartItemResponse> showShoppingCartData) {
		this.showShoppingCartData = showShoppingCartData;
	}
 
	public ShoppingCartPriceSummaryResponse getCartItemSummary() {
		return showShoppingCartSummary;
	}
	public void setCartItemSummary(ShoppingCartPriceSummaryResponse showShoppingCartSummary) {
		this.showShoppingCartSummary = showShoppingCartSummary;
	}
	public ShowShoppingCart(Integer totalItem, List<ShowShoppingCartItemResponse> showShoppingCartData,
			List<MessageDetail> shoppingCartMessage,  ShoppingCartPriceSummaryResponse showShoppingCartSummary) {
		super();
		this.totalItem = totalItem;
		this.showShoppingCartData = showShoppingCartData;
 		this.showShoppingCartSummary = showShoppingCartSummary;
	}
	public ShowShoppingCart() {
		super();
		// TODO Auto-generated constructor stub
	}
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
