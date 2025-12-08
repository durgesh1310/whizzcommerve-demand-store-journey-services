package com.ouat.customerService.customer.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalCreditUsedAndType {
	String type;
	Double totalCreditUsed;
}
