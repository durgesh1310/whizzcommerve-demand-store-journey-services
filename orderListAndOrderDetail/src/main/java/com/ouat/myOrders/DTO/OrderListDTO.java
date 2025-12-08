package com.ouat.myOrders.DTO;

import java.sql.Date;



import com.ouat.myOrders.response.OrderStatus;
import com.ouat.myOrders.response.RecordType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderListDTO{
	private String orderNumber;
	private int orderId;
	private String sku;
	private Date  orderDate;
	private int orderItemPayAmount;
	private OrderStatus orderStatus;
	private Date orderCancelledOrRefundRaisedDate;
	private Date orderRefundedDate;
	private Date orderShippedDate;
	private Date orderDeliveredDate;
	private Date orderReceavedDate;
	private int orderPayAmount;
	private Integer isRefundRequired ;
	private Integer isRefunded;
	private RecordType recordType;
	private Long orderItemId;
	private int qty;
	private String orderType;
	private Date rtoDate;
	private Date rtoDeliverDate;
	private String rtoTrackingNumber;

	
	 
	public OrderListDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderListDTO [orderId=" + orderId + ", sku=" + sku + ", orderDate=" + orderDate
				+ ", orderItemPayAmount=" + orderItemPayAmount + ", orderStatus=" + orderStatus
				+ ", orderCancelledOrRefundRaisedDate=" + orderCancelledOrRefundRaisedDate + ", orderRefundedDate="
				+ orderRefundedDate + ", orderShippedDate=" + orderShippedDate + ", orderDeliveredDate="
				+ orderDeliveredDate + ", orderReceavedDate=" + orderReceavedDate + ", orderPayAmount=" + orderPayAmount
				+ ", isRefundRequired=" + isRefundRequired + ", isRefunded=" + isRefunded + ", recordType=" + recordType
				+ ", orderItemId=" + orderItemId + "]";
	}
	



}
