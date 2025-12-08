package com.ouat.orderService.exchange.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCancelReturnRecordDto {
	
	private Long orderId;
	private Long orderItemId;
	private Integer qty;
	private Date requestedDate;
//	private Boolean isRefundRequired;
//	private Boolean isRefunded;
	private String recordType;
	private Integer reasonId;
	private String createdBy;
	private Date createdAt;
	

} 
