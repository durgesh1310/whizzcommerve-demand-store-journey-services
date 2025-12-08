package com.ouat.myOrders.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.myOrders.DTO.OrderDetailDTO;
import com.ouat.myOrders.DTO.OrderListDTO;
import com.ouat.myOrders.exception.BusinessProcessException;
import com.ouat.myOrders.response.OrderStatus;
import com.ouat.myOrders.response.RecordType;
import com.ouat.myOrders.sqlCommonConstant.SqlCommonConstant;

@Repository
public class MyOrdersRepository {
	public static final Logger LOGGER = LoggerFactory.getLogger(MyOrdersRepository.class);
	@Autowired 
	JdbcTemplate jdbcTemplate;
 
	RowMapper<OrderListDTO> orderListDetailRowMappper = (rs, rowNum) ->{
		return new OrderListDTO(
		        rs.getString("order_number"),
				rs.getInt("id"),
				rs.getString("sku"),
				rs.getDate("order_date"),
				rs.getInt("order_item_payable"),
				OrderStatus.valueOf(rs.getString("status")),
				rs.getDate("requested_date"),
				rs.getDate("refunded_date"),
				rs.getDate("shipped_date"),
				rs.getDate("delivered_date"),
				rs.getDate("created_at"),
				rs.getInt("order_payable"),
				rs.getInt("is_refund_required"),
				rs.getInt("is_refunded"),
				RecordType.valueOf(rs.getString("record_type")),
				rs.getLong("order_item_id"),
				rs.getInt("qty"),
				rs.getString("order_type"),
				rs.getDate("otd.rto_date"),
				rs.getDate("otd.rto_delivered_date"),
				rs.getString("otd.rto_tracking_no")
				);
	};
	public Map<Integer, List<OrderListDTO>> getOrderListDetail(long customerId,	Integer pageNo){
		
		List<OrderListDTO>orderListetail = null;
		
 		int from=(pageNo-1)*10;
		LOGGER.info("firing the query for order list for the customer id : {}, from page no : {}, to page no : {}",customerId, pageNo-1);
				orderListetail = jdbcTemplate.query(SqlCommonConstant.ORDER_LIST_DETAIL , orderListDetailRowMappper, customerId, from);
			if(orderListetail==null) {
				LOGGER.info("order list not successfully retreave from orders.order table");
				return null;
			}
			LOGGER.info("order list has successfully retreave from orders.order table with response : {}", orderListetail);
			Map<Integer, List<OrderListDTO>> map = new HashMap<>();
			for(OrderListDTO it1 : orderListetail) {
				int orderId = it1.getOrderId();
				if(!map.containsKey(orderId)) {
					List<OrderListDTO> orderListDto = new ArrayList<OrderListDTO>();
					for(OrderListDTO it2 : orderListetail) {
						 if(orderId == it2.getOrderId()) {
							 orderListDto.add(it2);
						 }
					 }
					map.put( orderId,orderListDto );
				}
			}
			LOGGER.info("order list has successfully retreave from orders.order table");
			return map;
 	}
	
	 
	RowMapper<OrderDetailDTO> orderItemDetailRowMappper = (rs, rowNum) ->{
		return new OrderDetailDTO(
				rs.getInt("is_returnable"),
				rs.getInt("is_exchangable"),
				rs.getDate("created_at"),
				rs.getInt("order_item_credit_applied"),
				rs.getInt("order_item_promo_discount"),
				rs.getInt("order_item_platform_offered_discount"),
				rs.getInt("order_item_payable"),
				rs.getInt("order_item_total_amount"),
				rs.getInt("qty"),
				rs.getString("sku"),
				rs.getLong("order_item_id"),
				rs.getDate("cancel_return_requested_date"),
				rs.getInt("is_refund_required"),
				rs.getInt("oocrr.is_refunded"),
				RecordType.valueOf(rs.getString("record_type")),
				rs.getDate("refunded_date"),
				rs.getString("refund_type"),
				rs.getInt("refund_amount"),
				rs.getInt("order_id"),
				rs.getString("order_number"),
				rs.getInt("customer_id"),
				rs.getDate("order_date"),
				rs.getString("promocode_applied"),
				rs.getString("payment_method"),
				rs.getInt("order_total_amount"),
				rs.getInt("order_payable"),
				rs.getInt("platform_offered_discount"),
				rs.getInt("promo_discount"),
				rs.getInt("credit_applied"),
				rs.getInt("shipping_charges"),
				OrderStatus.valueOf(rs.getString("order_item_status")),
				rs.getString("tracking_url"),
				rs.getInt("shipping_address_id"),
				rs.getString("order_type"),
				rs.getString("awb"),
				rs.getString("courrier_partner"),
				rs.getDate("shipped_date"),
				rs.getDate("delivered_date"),
				rs.getDate("ootd.rto_date"),
				rs.getDate("ootd.rto_delivered_date"),
				rs.getString("ootd.rto_tracking_no")
				);
	};
	public List<OrderDetailDTO> getOrderDetail(Integer customerId, Integer orderId, String orderNumber) throws BusinessProcessException {
        List<OrderDetailDTO> orderItemDetail = null;
		LOGGER.info("firing the query : {} for the order detail for the customer id : {}", customerId);

			  orderItemDetail  = jdbcTemplate.query(SqlCommonConstant.ORDER_DETAIL, orderItemDetailRowMappper ,orderId, orderNumber, customerId);
			LOGGER.info("query executed successfully  with response : {} ", orderItemDetail);
 		return orderItemDetail;
	}
	public Long isExchanged(Long orderItemId) {
		LOGGER.info(null);
		return jdbcTemplate.queryForObject( "Select * from orders.order_item where parent_id = ?",Long.class ,orderItemId);
		 
	}
	public Integer getOrderListCount(long customerId) {
		try {
			return jdbcTemplate.queryForObject("Select count(DISTINCT(oi.id)) from orders.order_item oi JOIN orders.order oo ON (oo.id = oi.order_id) where oo.customer_id = ? AND oo.order_status_id <> 1;", Integer.class, customerId);
		} catch (Exception e) {
		}
		return 0;

	}
}


