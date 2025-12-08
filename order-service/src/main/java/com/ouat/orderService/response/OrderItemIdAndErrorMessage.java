package com.ouat.orderService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemIdAndErrorMessage {
	private String orderItemId;
	private String errorMessage;


}
