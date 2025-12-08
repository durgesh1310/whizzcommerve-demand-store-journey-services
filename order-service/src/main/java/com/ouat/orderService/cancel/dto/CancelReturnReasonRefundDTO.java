package com.ouat.orderService.cancel.dto;

import java.util.List;

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
public class CancelReturnReasonRefundDTO {
	
	private Boolean isPopupRequired;
	
	private List<CancelReturnReasonDTO> reasons;

	@Override
	public String toString() {
		return "CancelReturnReasonRefundDTO [isPopupRequired=" + isPopupRequired + ", reasons=" + reasons + "]";
	}

	

	
}
