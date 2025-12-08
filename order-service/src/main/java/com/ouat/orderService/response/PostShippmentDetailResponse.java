package com.ouat.orderService.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostShippmentDetailResponse{
	private String status;
	List<OrderItemIdAndErrorMessage> orderItems;
	
}
