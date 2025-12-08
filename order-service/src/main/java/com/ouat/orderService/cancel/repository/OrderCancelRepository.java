package com.ouat.orderService.cancel.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.orderService.cancel.dto.CancelReturnReasonDTO;
import com.ouat.orderService.cancel.dto.OrderCancelReturnRecordDto;
import com.ouat.orderService.cancel.dto.OrderItemDTO;
import com.ouat.orderService.constants.CommonConstant;
import com.ouat.orderService.exception.BusinessProcessException;

@Repository
//TODO: Add logger
public class OrderCancelRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String GET_PAYMENT_METHOD = "SELECT payment_method FROM orders.order WHERE order_number = ?";

	public List<CancelReturnReasonDTO> getReason(String type) {
		List<CancelReturnReasonDTO> cancelReturnReason = new ArrayList<>();
		try {
			cancelReturnReason = jdbcTemplate.query(
					"Select id, reason from orders.order_cancel_return_reason where reason_type = ? and status=1",
					new RowMapper<CancelReturnReasonDTO>() {
						@Override
						public CancelReturnReasonDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new CancelReturnReasonDTO(rs.getInt("id"), rs.getString("reason"));
						}

					}, type);

		} catch (Exception e) {
		}
		return cancelReturnReason;
	}

	public Long getCustomerIdByOrderItemId(Long orderItemId, String cancelOrReturn) throws BusinessProcessException {
		try {
			String query = "SELECT o.customer_id FROM orders.order_item oi join orders.order o ON (o.id = oi.order_id) join orders.order_status os ON (os.id=oi.order_status_id) where oi.id = ? AND os.status = 'DL'";
			if("cancel".equals(cancelOrReturn)) {
				query = "SELECT o.customer_id FROM orders.order_item oi join orders.order o ON (o.id = oi.order_id) join orders.order_status os ON (os.id=oi.order_status_id) where oi.id = ? AND os.status IN ('OR', 'SV')";
			}
			return jdbcTemplate.queryForObject(
					query,
					Long.class, orderItemId);
		} catch (Exception e) {
			if("cancel".equals(cancelOrReturn))
				throw new BusinessProcessException(CommonConstant.ORDER_ALREADY_CANCELLED, CommonConstant.SUCCESS_STATUS_CODE);
			else if("return".equals(cancelOrReturn))
				throw new BusinessProcessException(CommonConstant.ORDER_ALREADY_RETURNED, CommonConstant.SUCCESS_STATUS_CODE);
			else 
				throw new BusinessProcessException(CommonConstant.ORDER_ALREADY_EXCHANGE, CommonConstant.SUCCESS_STATUS_CODE);
			
		}
	}

	
	public OrderItemDTO getOrderItemDetail(Long orderItemId) {
		OrderItemDTO orderItemDTO = null;
		try {
			orderItemDTO = jdbcTemplate.queryForObject(
					"Select o.order_number, oi.order_id, oi.sku, oi.qty, IFNULL(oi.order_item_payable, 0.0) as order_item_payable, IFNULL(oi.order_item_credit_applied, 0.0) as order_item_credit_applied, os.status, o.payment_method, oi.shipping_charges  FROM orders.order_item oi join orders.order_status os ON (oi.order_status_id=os.id) join orders.order o ON (o.id=oi.order_id) where oi.id = ?",
					new RowMapper<OrderItemDTO>() {
						@Override
                        public OrderItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new OrderItemDTO(rs.getInt("order_id"), rs.getString("sku"),
                                    rs.getInt("qty"), rs.getDouble("order_item_payable"),
                                    rs.getDouble("order_item_credit_applied"),
                                    rs.getString("status"), rs.getString("payment_method"),
                                    rs.getDouble("shipping_charges"), rs.getString("order_number"));
                        }
					}, orderItemId);
		} catch (Exception e) {
		}
		return orderItemDTO;

	}

	public int saveOrderCancelReturnRecord(OrderCancelReturnRecordDto orderCancelReturnRecordDto, boolean isReturn) {
		String refundType = "";
		if(orderCancelReturnRecordDto.getRefundType() == null) {
			refundType = null;
		}else {
			refundType = orderCancelReturnRecordDto.getRefundType().toString();
		}
		try {
			String createdBy = isReturn ? "order-service-return-api" : "order-service-cancel-api";
			String query = "insert into orders.order_cancel_return_record (order_id, order_item_id, qty, requested_date, is_refund_required, refund_amount, record_type, refund_type, reason_id, created_by, created_at) values (?, ?,?,?,?,?,?,?,?,?, CONVERT_TZ(now(), '+00:00', '+05:30'))";
			return jdbcTemplate.update(query, orderCancelReturnRecordDto.getOrderId(),
					orderCancelReturnRecordDto.getOrderItemId(), orderCancelReturnRecordDto.getQty(),
					orderCancelReturnRecordDto.getRequestedDate(), orderCancelReturnRecordDto.isRefundRequired(),
					orderCancelReturnRecordDto.getRefundAmount(), orderCancelReturnRecordDto.getRecordType().toString(),
					refundType, orderCancelReturnRecordDto.getReasonId(),
					createdBy);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateOrderItemStatus(Long orderItemId, Integer orderStatusId) {
		String query = "update orders.order_item set order_status_id = ?, updated_by = ?, updated_at = CONVERT_TZ(now(), '+00:00', '+05:30') where id = ?";
		return jdbcTemplate.update(query, orderStatusId, "order-service-cancel-api", orderItemId);
	}
	
	public String paymentMethod(String orderNumber) {
		return jdbcTemplate.queryForObject(GET_PAYMENT_METHOD, String.class, orderNumber);
	}

}
