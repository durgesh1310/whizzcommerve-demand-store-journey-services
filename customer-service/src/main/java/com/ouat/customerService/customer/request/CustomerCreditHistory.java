package com.ouat.customerService.customer.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCreditHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2453079656525141702L;

	private Long customerId;
	
	private Long orderId;
	 
	private Double totalCreditUsed;

	private String type;
		
	private List<OrderItemCreditDetail> orderItemCreditDetail;
	 
	
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
