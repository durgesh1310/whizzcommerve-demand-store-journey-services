package com.ouat.customerService.customer.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EmailAddRequest {
	
	@NotNull
	private String email;

}
