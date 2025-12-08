package com.ouat.orderService.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class InvoiceDetailDto {
	
		@NotNull
		private String orderItemId;
		@NotNull
		private String invoiceNumber;
		@NotNull
		private Date invoiceDate;
		public String getOrderItemId() {
			return orderItemId;
		}
		public void setOrderItemId(String orderItemId) {
			this.orderItemId = orderItemId;
		}
		public String getInvoiceNumber() {
			return invoiceNumber;
		}
		public void setInvoiceNumber(String invoiceNumber) {
			this.invoiceNumber = invoiceNumber;
		}
		public Date getInvoiceDate() {
			return invoiceDate;
		}
		public void setInvoiceDate(Date invoiceDate) {
			this.invoiceDate = invoiceDate;
		}
		@Override
		public String toString() {
			return "OrderItemsDetail [orderItemId=" + orderItemId + ", invoiceNumber=" + invoiceNumber
					+ ", invoiceDate=" + invoiceDate + "]";
		}
		

}
