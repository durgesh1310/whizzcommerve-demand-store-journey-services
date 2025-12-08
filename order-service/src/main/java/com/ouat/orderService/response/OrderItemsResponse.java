package com.ouat.orderService.response;

import javax.validation.constraints.NotNull;

public class OrderItemsResponse{
	@NotNull
	private String orderItemId;
	@NotNull
	private String errorMessage;
	 
}

